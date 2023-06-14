package com.example.backendtestproject.services;

import com.example.backendtestproject.dtos.ProgrammerDetailsDto;
import com.example.backendtestproject.dtos.ProgrammerDto;
import com.example.backendtestproject.dtos.ValidatorResultDto;
import com.example.backendtestproject.enums.Responsibility;
import com.example.backendtestproject.models.Address;
import com.example.backendtestproject.models.BirthDate;
import com.example.backendtestproject.models.Programmer;
import com.example.backendtestproject.models.Project;
import com.example.backendtestproject.repositories.ProgrammerRepository;
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

public class ProgrammerServiceImplTest {
    private ProgrammerRepository mockProgrammerRepository;
    private ValidatorService mockValidatorService;
    private ModelMapper modelMapper;
    private ProjectManagerRepository projectManagerRepository;
    private ProgrammerService target;
    private Programmer programmer;
    private List<Programmer> programmers;

    @BeforeEach
    void init() {
        mockProgrammerRepository = Mockito.mock(ProgrammerRepository.class);
        mockValidatorService = Mockito.mock(ValidatorService.class);
        modelMapper = new ModelMapper();
        target = new ProgrammerServiceImpl(mockProgrammerRepository, projectManagerRepository, mockValidatorService, modelMapper);
        programmer = Programmer.builder()
                .name("First Programmer")
                .email("firstprogrammer@gmail.com")
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
                .responsibility(Responsibility.BACKEND)
                .isApprentice(true)
                .deleted(false).build();

        programmers = List.of(
                Programmer.builder()
                        .name("First Programmer")
                        .email("firstprogrammer@gmail.com")
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
                        .responsibility(Responsibility.BACKEND)
                        .isApprentice(true)
                        .deleted(false).build(),

                Programmer.builder()
                        .name("Second Programmer")
                        .email("secondprogrammer@gmail.com")
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
                        .responsibility(Responsibility.BACKEND)
                        .isApprentice(true)
                        .deleted(false).build());
    }

    @Test
    void save_ProgrammerWithValidFields_GetsValidatedAndSucceedsToSaveAndReturnsCorrectDto() {

        // ARRANGE
        ValidatorResultDto validationResult = new ValidatorResultDto(true, "success");
        Mockito.when(mockValidatorService.isProgrammerValid(any(ProgrammerDto.class))).thenReturn(validationResult);
        ProgrammerDto input = modelMapper.map(programmer, ProgrammerDto.class);

        // ACT
        ValidatorResultDto actualResult = target.save(input);

        // ASSERT
        assertEquals(validationResult.isValid(), actualResult.isValid());
        assertEquals(validationResult.message(), actualResult.message());
        Mockito.verify(mockValidatorService, Mockito.times(1)).isProgrammerValid(input);
        Mockito.verify(mockProgrammerRepository, Mockito.times(1)).save(any(Programmer.class));
    }

    @Test
    void save_ProgrammerWithInvalidField_GetsValidatedAndFailsToSaveAndReturnsCorrectDto() {

        // ARRANGE
        ValidatorResultDto validationResult = new ValidatorResultDto(false, "failure");
        Mockito.when(mockValidatorService.isProgrammerValid(any(ProgrammerDto.class))).thenReturn(validationResult);
        ProgrammerDto input = modelMapper.map(programmer, ProgrammerDto.class);

        // ACT
        ValidatorResultDto actualResult = target.save(input);

        // ASSERT
        assertEquals(validationResult.isValid(), actualResult.isValid());
        assertEquals(validationResult.message(), actualResult.message());
        Mockito.verify(mockValidatorService, Mockito.times(1)).isProgrammerValid(input);
        Mockito.verify(mockProgrammerRepository, Mockito.never()).save(any(Programmer.class));
    }

    @Test
    void findAll_ListContainsProgrammers_ReturnsProgrammerDtoList() {

        // ARRANGE
        List<ProgrammerDto> expectedResult = programmers.stream().map(r -> modelMapper.map(r, ProgrammerDto.class)).toList();

        Mockito.when(mockProgrammerRepository.findAll()).thenReturn(programmers);

        // ACT
        List<ProgrammerDto> actualResult = target.findAll();

        // ASSERT
        assertEquals(expectedResult.size(), actualResult.size());

        for (int i = 0; i < actualResult.size(); i++) {
            assertThat(actualResult.get(i), samePropertyValuesAs(expectedResult.get(i), "address", "birthDate"));
            assertThat(actualResult.get(i).getAddress(), samePropertyValuesAs(expectedResult.get(i).getAddress()));
        }

        Mockito.verify(mockProgrammerRepository, Mockito.times(1)).findAll();
    }

    @Test
    void findAll_EmptyList_ReturnsEmptyProgrammerDtoList() {

        // ARRANGE
        Mockito.when(mockProgrammerRepository.findAll()).thenReturn(new ArrayList<>());

        // ACT
        List<ProgrammerDto> actualResult = target.findAll();

        // ASSERT
        assertEquals(0, actualResult.size());

        Mockito.verify(mockProgrammerRepository, Mockito.times(1)).findAll();
    }

    @Test
    void findById_ValidId_ReturnsProgrammerDetailsDto() {

        // ARRANGE
        ProgrammerDetailsDto expectedResult = modelMapper.map(programmer, ProgrammerDetailsDto.class);
        Mockito.when(mockProgrammerRepository.findByProgrammerId(any(Long.class))).thenReturn(programmer);

        // ACT
        ProgrammerDetailsDto actualResult = target.findById(1L);

        // ASSERT
        assertThat(actualResult, samePropertyValuesAs(expectedResult, "address", "birthDate", "project"));

        assertThat(actualResult.getAddress(), samePropertyValuesAs(expectedResult.getAddress()));

        assertThat(actualResult.getBirthDate(), samePropertyValuesAs(expectedResult.getBirthDate()));

        assertThat(actualResult.getProject(), samePropertyValuesAs(expectedResult.getProject()));

        Mockito.verify(mockProgrammerRepository, Mockito.times(1)).findByProgrammerId(1L);
    }

    @Test
    void findAllSorted_WithIncorrectField_ReturnsOriginalProgrammerDtoList() {

        // ARRANGE
        Mockito.when(mockProgrammerRepository.findAll()).thenReturn(programmers);

        List<ProgrammerDto> expectedResult = programmers
                .stream()
                .map(r -> modelMapper.map(r, ProgrammerDto.class))
                .toList();

        // ACT
        List<ProgrammerDto> actualResult = target.findAllSorted("nonExistingField", "desc");

        // ASSERT
        assertEquals(expectedResult.size(), actualResult.size());

        for (int i = 0; i < actualResult.size(); i++) {
            assertThat(actualResult.get(i), samePropertyValuesAs(expectedResult.get(i), "address", "birthDate"));
            assertThat(actualResult.get(i).getAddress(), samePropertyValuesAs(expectedResult.get(i).getAddress()));
        }

        Mockito.verify(mockProgrammerRepository, Mockito.times(1)).findAll();
    }

    @Test
    void findAllSortedByName_ListContainsProgrammers_ReturnsProgrammerDtoList() {

        // ARRANGE
        Mockito.when(mockProgrammerRepository.findAll()).thenReturn(programmers);

        List<ProgrammerDto> expectedResult = programmers
                .stream()
                .sorted(Comparator.comparing(Programmer::getName).reversed())
                .map(r -> modelMapper.map(r, ProgrammerDto.class))
                .toList();

        // ACT
        List<ProgrammerDto> actualResult = target.findAllSorted("name", "desc");

        // ASSERT
        assertEquals(expectedResult.size(), actualResult.size());

        for (int i = 0; i < actualResult.size(); i++) {
            assertThat(actualResult.get(i), samePropertyValuesAs(expectedResult.get(i), "address", "birthDate"));
            assertThat(actualResult.get(i).getAddress(), samePropertyValuesAs(expectedResult.get(i).getAddress()));
        }

        Mockito.verify(mockProgrammerRepository, Mockito.times(1)).findAll();
    }

    @Test
    void deleteById_ValidId_GetsDeletedAndReturnsTrue() {

        // ARRANGE
        Mockito.when(mockProgrammerRepository.findByProgrammerId(Mockito.anyLong())).thenReturn(programmer);

        // ACT
        boolean actualResult = target.deleteById(1L);

        // ASSERT
        assertTrue(actualResult);
        Mockito.verify(mockProgrammerRepository, Mockito.times(1)).findByProgrammerId(1L);
        Mockito.verify(mockProgrammerRepository, Mockito.times(1)).deleteByProgrammerId(1L);
    }
}
