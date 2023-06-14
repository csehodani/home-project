package com.example.backendtestproject.services;

import com.example.backendtestproject.dtos.ProjectManagerDetailsDto;
import com.example.backendtestproject.dtos.ProjectManagerDto;
import com.example.backendtestproject.dtos.ValidatorResultDto;
import com.example.backendtestproject.models.Address;
import com.example.backendtestproject.models.BirthDate;
import com.example.backendtestproject.models.Project;
import com.example.backendtestproject.models.ProjectManager;
import com.example.backendtestproject.repositories.ProjectManagerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

public class ProjectManagerServiceImplTest {

    private ProjectManagerRepository mockProjectManagerRepository;
    private ValidatorService mockValidatorService;
    private ModelMapper modelMapper;
    private ProjectManagerService target;
    private ProjectManager projectManager;
    private List<ProjectManager> projectManagers;

    @BeforeEach
    void init() {
        mockProjectManagerRepository = Mockito.mock(ProjectManagerRepository.class);
        mockValidatorService = Mockito.mock(ValidatorService.class);
        modelMapper = new ModelMapper();
        target = new ProjectManagerServiceImpl(mockProjectManagerRepository, mockValidatorService, modelMapper);
        projectManager = ProjectManager.builder()
                .name("First Manager")
                .email("firstmanager@gmail.com")
                .address(Address.builder()
                        .city("Budapest")
                        .street("Test street")
                        .zipCode(1000).build())
                .birthDate(BirthDate.builder()
                        .day(11)
                        .month(3)
                        .year(1999).build())
                .phoneNumber("+36203456789")
                .project(Project.builder()
                        .client("Test Client")
                        .startDate("2162")
                        .description("Test project").build())
                .deleted(false).build();

        projectManagers = List.of(
                ProjectManager.builder()
                        .name("First Manager")
                        .email("firstmanager@gmail.com")
                        .address(Address.builder()
                                .city("Budapest")
                                .street("Test street")
                                .zipCode(1000).build())
                        .birthDate(BirthDate.builder()
                                .day(11)
                                .month(3)
                                .year(1999).build())
                        .phoneNumber("+36203456789")
                        .project(Project.builder()
                                .client("Test Client")
                                .startDate("2162")
                                .description("Test project").build())
                        .deleted(false).build(),

                ProjectManager.builder()
                        .name("Second Manager")
                        .email("secondmanager@gmail.com")
                        .address(Address.builder()
                                .city("Budapest")
                                .street("Test street")
                                .zipCode(1000).build())
                        .birthDate(BirthDate.builder()
                                .day(11)
                                .month(3)
                                .year(1999).build())
                        .phoneNumber("+36503456789")
                        .project(Project.builder()
                                .client("Tester")
                                .startDate("2165")
                                .description("test project").build())
                        .deleted(false).build());
    }

    @Test
    void save_ProjectManagerWithValidFields_GetsValidatedAndSucceedsToSaveAndReturnsCorrectDto() {

        // ARRANGE
        ValidatorResultDto validationResult = new ValidatorResultDto(true, "success");
        Mockito.when(mockValidatorService.isProjectManagerValid(any(ProjectManagerDto.class))).thenReturn(validationResult);
        ProjectManagerDto input = modelMapper.map(projectManager, ProjectManagerDto.class);

        // ACT
        ValidatorResultDto actualResult = target.save(input);

        // ASSERT
        assertEquals(validationResult.isValid(), actualResult.isValid());
        assertEquals(validationResult.message(), actualResult.message());
        Mockito.verify(mockValidatorService, Mockito.times(1)).isProjectManagerValid(input);
        Mockito.verify(mockProjectManagerRepository, Mockito.times(1)).save(any(ProjectManager.class));
    }

    @Test
    void save_ProjectManagerWithInvalidField_GetsValidatedAndFailsToSaveAndReturnsCorrectDto() {

        // ARRANGE
        ValidatorResultDto validationResult = new ValidatorResultDto(false, "failure");
        Mockito.when(mockValidatorService.isProjectManagerValid(any(ProjectManagerDto.class))).thenReturn(validationResult);
        ProjectManagerDto input = modelMapper.map(projectManager, ProjectManagerDto.class);

        // ACT
        ValidatorResultDto actualResult = target.save(input);

        // ASSERT
        assertEquals(validationResult.isValid(), actualResult.isValid());
        assertEquals(validationResult.message(), actualResult.message());
        Mockito.verify(mockValidatorService, Mockito.times(1)).isProjectManagerValid(input);
        Mockito.verify(mockProjectManagerRepository, Mockito.never()).save(any(ProjectManager.class));
    }

    @Test
    void findAll_ListContainsProjectManagers_ReturnsProjectManagerDtoList() {

        // ARRANGE
        List<ProjectManagerDto> expectedResult = projectManagers.stream().map(r -> modelMapper.map(r, ProjectManagerDto.class)).toList();

        Mockito.when(mockProjectManagerRepository.findAll()).thenReturn(projectManagers);

        // ACT
        List<ProjectManagerDto> actualResult = target.findAll();

        // ASSERT
        assertEquals(expectedResult.size(), actualResult.size());

        for (int i = 0; i < actualResult.size(); i++) {
            assertThat(actualResult.get(i), samePropertyValuesAs(expectedResult.get(i), "address", "birthDate"));
            assertThat(actualResult.get(i).getAddress(), samePropertyValuesAs(expectedResult.get(i).getAddress()));
        }

        Mockito.verify(mockProjectManagerRepository, Mockito.times(1)).findAll();
    }

    @Test
    void findAll_EmptyList_ReturnsEmptyProjectManagerDtoList() {

        // ARRANGE
        Mockito.when(mockProjectManagerRepository.findAll()).thenReturn(new ArrayList<>());

        // ACT
        List<ProjectManagerDto> actualResult = target.findAll();

        // ASSERT
        assertEquals(0, actualResult.size());

        Mockito.verify(mockProjectManagerRepository, Mockito.times(1)).findAll();
    }

    @Test
    void findById_ValidId_ReturnsProjectManagerDetailsDto() {

        // ARRANGE
        ProjectManagerDetailsDto expectedResult = modelMapper.map(projectManager, ProjectManagerDetailsDto.class);
        Mockito.when(mockProjectManagerRepository.findByProjectManagerId(any(Long.class))).thenReturn(projectManager);

        // ACT
        ProjectManagerDetailsDto actualResult = target.findById(1L);

        // ASSERT
        assertThat(actualResult, samePropertyValuesAs(expectedResult, "address", "birthDate", "project"));

        assertThat(actualResult.getAddress(), samePropertyValuesAs(expectedResult.getAddress()));

        assertThat(actualResult.getBirthDate(), samePropertyValuesAs(expectedResult.getBirthDate()));

        assertThat(actualResult.getProject(), samePropertyValuesAs(expectedResult.getProject()));

        Mockito.verify(mockProjectManagerRepository, Mockito.times(1)).findByProjectManagerId(1L);
    }

    @Test
    void findAllSorted_WithIncorrectField_ReturnsOriginalProjectManagerDtoList() {

        // ARRANGE
        Mockito.when(mockProjectManagerRepository.findAll()).thenReturn(projectManagers);

        List<ProjectManagerDto> expectedResult = projectManagers
                .stream()
                .map(r -> modelMapper.map(r, ProjectManagerDto.class))
                .toList();

        // ACT
        List<ProjectManagerDto> actualResult = target.findAllSorted("nonExistingField", "desc");

        // ASSERT
        assertEquals(expectedResult.size(), actualResult.size());

        for (int i = 0; i < actualResult.size(); i++) {
            assertThat(actualResult.get(i), samePropertyValuesAs(expectedResult.get(i), "address", "birthDate"));
            assertThat(actualResult.get(i).getAddress(), samePropertyValuesAs(expectedResult.get(i).getAddress()));
        }

        Mockito.verify(mockProjectManagerRepository, Mockito.times(1)).findAll();
    }

    @Test
    void findAllSortedByName_ListContainsProjectManagers_ReturnsProjectManagerDtoList() {

        // ARRANGE
        Mockito.when(mockProjectManagerRepository.findAll()).thenReturn(projectManagers);

        List<ProjectManagerDto> expectedResult = projectManagers
                .stream()
                .sorted(Comparator.comparing(ProjectManager::getName).reversed())
                .map(r -> modelMapper.map(r, ProjectManagerDto.class))
                .toList();

        // ACT
        List<ProjectManagerDto> actualResult = target.findAllSorted("name", "desc");

        // ASSERT
        assertEquals(expectedResult.size(), actualResult.size());

        for (int i = 0; i < actualResult.size(); i++) {
            assertThat(actualResult.get(i), samePropertyValuesAs(expectedResult.get(i), "address", "birthDate"));
            assertThat(actualResult.get(i).getAddress(), samePropertyValuesAs(expectedResult.get(i).getAddress()));
        }

        Mockito.verify(mockProjectManagerRepository, Mockito.times(1)).findAll();
    }

    @Test
    void deleteById_ValidId_GetsDeletedAndReturnsTrue() {

        // ARRANGE
        Mockito.when(mockProjectManagerRepository.findByProjectManagerId(Mockito.anyLong())).thenReturn(projectManager);

        // ACT
        boolean actualResult = target.deleteById(1L);

        // ASSERT
        assertTrue(actualResult);
        Mockito.verify(mockProjectManagerRepository, Mockito.times(1)).findByProjectManagerId(1L);
        Mockito.verify(mockProjectManagerRepository, Mockito.times(1)).deleteByProjectManagerId(1L);
    }
}
