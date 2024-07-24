package com.example.demo.github.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@Value
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GitHubRepositoryBranchDto {
    String name;
    String lastCommitSha;
}
