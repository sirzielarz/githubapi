package com.example.demo.github.controller;

import com.example.demo.github.dto.GitHubRepositoryDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/github")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
class GitHubController {


    GitHubService githubService;

    @GetMapping("/users/{username}/repos")
    public ResponseEntity<List<GitHubRepositoryDto>> getUserRepos(@PathVariable String username) {

        return ResponseEntity.ok(githubService.listRepositories(username));
    }
}
