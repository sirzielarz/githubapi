package com.example.demo;

import com.example.demo.github.controller.GitHubService;
import com.example.demo.github.dto.GitHubRepositoryDto;
import com.example.demo.github.exception.UserNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GitHubServiceTest {

    @InjectMocks
    private GitHubService gitHubService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Value("${github.token}")
    private String githubToken;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListRepositories() throws Exception {
        String username = "test-user";
        String url = "https://api.github.com/users/" + username + "/repos";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + githubToken);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String jsonResponse = "[{\"name\":\"test-repository\",\"owner\":{\"login\":\"test-owner\"},\"fork\":false}]";
        ResponseEntity<String> responseEntity = ResponseEntity.ok(jsonResponse);

        when(restTemplate.exchange(url, HttpMethod.GET, entity, String.class)).thenReturn(responseEntity);

        JsonNode rootNode = new ObjectMapper().readTree(jsonResponse);
        when(objectMapper.readTree(jsonResponse)).thenReturn(rootNode);

        List<GitHubRepositoryDto> repositories = gitHubService.listRepositories(username);

        assertNotNull(repositories);
        assertEquals(1, repositories.size());
        assertEquals("test-repository", repositories.getFirst().getName());
        assertEquals("test-owner", repositories.getFirst().getOwnerLogin());
    }

    @Test
    void testListRepositoriesUserNotFound() {
        String username = "user-that-certainly-does-not-exist-123";
        String url = "https://api.github.com/users/" + username + "/repos";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + githubToken);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        when(restTemplate.exchange(url, HttpMethod.GET, entity, String.class))
                .thenThrow(new UserNotFoundException("User not found"));

        assertThrows(UserNotFoundException.class, () -> gitHubService.listRepositories(username));
    }
}
