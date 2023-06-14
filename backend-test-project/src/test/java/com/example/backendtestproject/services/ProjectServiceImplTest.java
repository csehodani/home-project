package com.example.backendtestproject.services;

import com.example.backendtestproject.dtos.ProjectDto;
import com.example.backendtestproject.dtos.ValidatorResultDto;
import com.example.backendtestproject.models.Project;
import com.example.backendtestproject.repositories.ProgrammerRepository;
import com.example.backendtestproject.repositories.ProjectManagerRepository;
import com.example.backendtestproject.repositories.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

public class ProjectServiceImplTest {
    private ProjectRepository mockProjectRepository;
    private ProjectManagerRepository mockProjectManagerRepository;
    private ProgrammerRepository mockProgrammerRepository;
    private ValidatorService mockValidatorService;
    private ModelMapper modelMapper;
    private ProjectService target;
    private Project project;
    private List<Project> projects;

    @BeforeEach
    void init() {
        mockProjectRepository = Mockito.mock(ProjectRepository.class);
        mockValidatorService = Mockito.mock(ValidatorService.class);
        modelMapper = new ModelMapper();
        target = new ProjectServiceImpl(mockProjectRepository, modelMapper, mockValidatorService, mockProjectManagerRepository, mockProgrammerRepository);
        project = Project.builder()
                .client("First Client")
                .startDate("2023")
                .description("First")
                .deleted(false).build();

        projects = List.of(
                Project.builder()
                        .client("First Client")
                        .startDate("2023")
                        .description("First")
                        .deleted(false).build(),

                Project.builder()
                        .client("Second Client")
                        .startDate("2023")
                        .description("Second")
                        .deleted(false).build());
    }

    @Test
    void save_ProjectWithValidFields_GetsValidatedAndSucceedsToSaveAndReturnsCorrectDto() {

        // ARRANGE
        ValidatorResultDto validationResult = new ValidatorResultDto(true, "success");
        Mockito.when(mockValidatorService.isProjectValid(any(ProjectDto.class))).thenReturn(validationResult);
        ProjectDto input = modelMapper.map(project, ProjectDto.class);

        // ACT
        ValidatorResultDto actualResult = target.save(input);

        // ASSERT
        assertEquals(validationResult.isValid(), actualResult.isValid());
        assertEquals(validationResult.message(), actualResult.message());
        Mockito.verify(mockValidatorService, Mockito.times(1)).isProjectValid(input);
        Mockito.verify(mockProjectRepository, Mockito.times(1)).save(any(Project.class));
    }

    @Test
    void save_ProjectWithInvalidField_GetsValidatedAndFailsToSaveAndReturnsCorrectDto() {

        // ARRANGE
        ValidatorResultDto validationResult = new ValidatorResultDto(false, "failure");
        Mockito.when(mockValidatorService.isProjectValid(any(ProjectDto.class))).thenReturn(validationResult);
        ProjectDto input = modelMapper.map(project, ProjectDto.class);

        // ACT
        ValidatorResultDto actualResult = target.save(input);

        // ASSERT
        assertEquals(validationResult.isValid(), actualResult.isValid());
        assertEquals(validationResult.message(), actualResult.message());
        Mockito.verify(mockValidatorService, Mockito.times(1)).isProjectValid(input);
        Mockito.verify(mockProjectRepository, Mockito.never()).save(any(Project.class));
    }

    @Test
    void findAll_ListContainsProjects_ReturnsProjectDtoList() {

        // ARRANGE
        List<ProjectDto> expectedResult = projects.stream().map(r -> modelMapper.map(r, ProjectDto.class)).toList();

        Mockito.when(mockProjectRepository.findAll()).thenReturn(projects);

        // ACT
        List<ProjectDto> actualResult = target.findAll();

        // ASSERT
        assertEquals(expectedResult.size(), actualResult.size());

        Mockito.verify(mockProjectRepository, Mockito.times(1)).findAll();
    }

    @Test
    void findAll_EmptyList_ReturnsEmptyProjectDtoList() {

        // ARRANGE
        Mockito.when(mockProjectRepository.findAll()).thenReturn(new ArrayList<>());

        // ACT
        List<ProjectDto> actualResult = target.findAll();

        // ASSERT
        assertEquals(0, actualResult.size());

        Mockito.verify(mockProjectRepository, Mockito.times(1)).findAll();
    }

    @Test
    void findById_ValidId_ReturnsProjectManagerDetailsDto() {

        // ARRANGE
        ProjectDto expectedResult = modelMapper.map(project, ProjectDto.class);
        Mockito.when(mockProjectRepository.findByProjectId(any(Long.class))).thenReturn(project);

        // ACT
        ProjectDto actualResult = target.findById(1L);

        // ASSERT
        Mockito.verify(mockProjectRepository, Mockito.times(1)).findByProjectId(1L);
    }

    @Test
    void findAllSorted_WithIncorrectField_ReturnsOriginalProjectDtoList() {

        // ARRANGE
        Mockito.when(mockProjectRepository.findAll()).thenReturn(projects);

        List<ProjectDto> expectedResult = projects
                .stream()
                .map(r -> modelMapper.map(r, ProjectDto.class))
                .toList();

        // ACT
        List<ProjectDto> actualResult = target.findAllSorted("nonExistingField", "desc");

        // ASSERT
        assertEquals(expectedResult.size(), actualResult.size());

        Mockito.verify(mockProjectRepository, Mockito.times(1)).findAll();
    }

    @Test
    void findAllSortedByClient_ListContainsProjects_ReturnsProjectDtoList() {

        // ARRANGE
        Mockito.when(mockProjectRepository.findAll()).thenReturn(projects);

        List<ProjectDto> expectedResult = projects
                .stream()
                .sorted(Comparator.comparing(Project::getClient).reversed())
                .map(r -> modelMapper.map(r, ProjectDto.class))
                .toList();

        // ACT
        List<ProjectDto> actualResult = target.findAllSorted("client", "desc");

        // ASSERT
        assertEquals(expectedResult.size(), actualResult.size());

        Mockito.verify(mockProjectRepository, Mockito.times(1)).findAll();
    }

    @Test
    void deleteById_ValidId_GetsDeletedAndReturnsTrue() {

        // ARRANGE
        Mockito.when(mockProjectRepository.findByProjectId(Mockito.anyLong())).thenReturn(project);

        // ACT
        boolean actualResult = target.deleteById(1L);

        // ASSERT
        assertTrue(actualResult);
        Mockito.verify(mockProjectRepository, Mockito.times(1)).findByProjectId(1L);
        Mockito.verify(mockProjectRepository, Mockito.times(1)).deleteByProjectId(1L);
    }
}
