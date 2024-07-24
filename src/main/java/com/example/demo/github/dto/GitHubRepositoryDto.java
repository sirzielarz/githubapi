package com.example.demo.github.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Value
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GitHubRepositoryDto {
    String name;
    String ownerLogin;
    List<GitHubRepositoryBranchDto> branches;
}
