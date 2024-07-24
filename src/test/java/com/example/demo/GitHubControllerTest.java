package com.example.demo;

import com.example.demo.github.Application;
import com.example.demo.github.controller.GitHubService;
import com.example.demo.github.dto.GitHubRepositoryDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Application.class})
@AutoConfigureMockMvc
class GitHubControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private GitHubService gitHubService;

    @Test
    void testGetUserRepos() throws Exception {
        var repositoryBuilder = GitHubRepositoryDto.builder();
        repositoryBuilder.name("test-repository");
        repositoryBuilder.ownerLogin("test-owner");
        repositoryBuilder.branches(Collections.emptyList());


        Mockito.when(gitHubService.listRepositories("test-owner"))
                .thenReturn(Collections.singletonList(repositoryBuilder.build()));

        mockMvc.perform(get("/api/github/users/test-owner/repos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'name':'test-repository','ownerLogin':'test-owner','branches':[]}]"));
    }
}

