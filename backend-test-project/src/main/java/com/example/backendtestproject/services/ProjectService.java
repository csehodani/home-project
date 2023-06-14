package com.example.backendtestproject.services;

import com.example.backendtestproject.dtos.ProjectDto;
import com.example.backendtestproject.dtos.ValidatorResultDto;

import java.util.List;

public interface ProjectService {

    ValidatorResultDto save(ProjectDto project);

    List<ProjectDto> findAll();

    List<ProjectDto> findAllSorted(String sortBy, String order);

    ProjectDto findById(Long id);

    ValidatorResultDto editById(Long id, ProjectDto project);

    ValidatorResultDto saveByProjectManagerId(ProjectDto project, Long projectManagerId);

    ValidatorResultDto saveByProgrammerId(ProjectDto project, Long programmerId);

    boolean deleteById(Long id);
}
