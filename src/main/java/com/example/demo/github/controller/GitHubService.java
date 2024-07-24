package com.example.demo.github.controller;

import com.example.demo.github.exception.UserNotFoundException;
import com.example.demo.github.dto.GitHubRepositoryBranchDto;
import com.example.demo.github.dto.GitHubRepositoryDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class GitHubService {

    final RestTemplate restTemplate;
    final ObjectMapper objectMapper;

    @Value("${github.token}")
    String githubToken;

    public List<GitHubRepositoryDto> listRepositories(String username) {
        String url = "https://api.github.com/users/" + username + "/repos";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + githubToken);
        headers.set("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new UserNotFoundException("User not found");
        }
        List<GitHubRepositoryDto> repositories = new ArrayList<>();
        try {
            JsonNode readTree = objectMapper.readTree(response.getBody());
            for (JsonNode node : readTree) {
                if (!node.get("fork").asBoolean()) {
                    var repositoryBuilder = GitHubRepositoryDto.builder();
                    repositoryBuilder.name(node.get("name").asText());
                    repositoryBuilder.ownerLogin(node.get("owner").get("login").asText());
                    repositoryBuilder.branches(getBranches(node.get("owner").get("login").asText(), node.get("name").asText()));
                    repositories.add(repositoryBuilder.build());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return repositories;
    }

    private List<GitHubRepositoryBranchDto> getBranches(String owner, String repoName) {
        String url = "https://api.github.com/repos/" + owner + "/" + repoName + "/branches";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + githubToken);
        headers.set("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        List<GitHubRepositoryBranchDto> branches = new ArrayList<>();
        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            for (JsonNode node : root) {
                var branchBuilder = GitHubRepositoryBranchDto.builder();
                branchBuilder.name(node.get("name").asText());
                branchBuilder.lastCommitSha(node.get("commit").get("sha").asText());
                branches.add(branchBuilder.build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return branches;
    }
}
