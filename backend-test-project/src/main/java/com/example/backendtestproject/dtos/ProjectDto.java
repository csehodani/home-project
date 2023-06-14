package com.example.backendtestproject.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProjectDto {
    private Long projectId;

    private String client;

    private String startDate;

    private String description;

    private Boolean deleted = Boolean.FALSE;
}
