package com.example.backendtestproject.services;

import com.example.backendtestproject.dtos.ProjectManagerDetailsDto;
import com.example.backendtestproject.dtos.ProjectManagerDto;
import com.example.backendtestproject.dtos.ValidatorResultDto;

import java.util.List;

public interface ProjectManagerService {
    ValidatorResultDto save(ProjectManagerDto projectManager);

    List<ProjectManagerDto> findAll();

    List<ProjectManagerDto> findAllSorted(String sortBy, String order);

    ProjectManagerDetailsDto findById(Long id);

    ValidatorResultDto editById(Long id, ProjectManagerDto projectManager);

    boolean deleteById(Long id);
}
