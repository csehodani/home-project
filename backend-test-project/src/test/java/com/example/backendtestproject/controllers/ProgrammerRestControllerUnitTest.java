package com.example.backendtestproject.controllers;

import com.example.backendtestproject.constants.ProgrammingConstants;
import com.example.backendtestproject.dtos.ProgrammerDetailsDto;
import com.example.backendtestproject.dtos.ProgrammerDto;
import com.example.backendtestproject.dtos.ValidatorResultDto;
import com.example.backendtestproject.enums.Responsibility;
import com.example.backendtestproject.models.Address;
import com.example.backendtestproject.models.BirthDate;
import com.example.backendtestproject.models.Programmer;
import com.example.backendtestproject.models.Project;
import com.example.backendtestproject.services.ProgrammerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProgrammerRestController.class)
@AutoConfigureMockMvc
public class ProgrammerRestControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProgrammerService programmerService;

    private ModelMapper modelMapper;

    private Programmer programmer;

    private List programmers;

    @BeforeEach
    public void init() {
        modelMapper = new ModelMapper();
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
                                .description("Test project")
                                .deleted(false).build())
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
                                .description("test project")
                                .deleted(false).build())
                        .responsibility(Responsibility.FRONTEND)
                        .isApprentice(true)
                        .deleted(false).build());

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
                        .description("Test project")
                        .deleted(false).build())
                .responsibility(Responsibility.BACKEND)
                .isApprentice(true)
                .deleted(false).build();

    }

    @Test
    public void apiListProgrammers_WithProgrammers_ReturnsList() throws Exception {

        Mockito.when(programmerService.findAllSorted(Mockito.isNull(), Mockito.isNull())).thenReturn(programmers);

        mockMvc.perform(get("/api/programmers"))
                .andExpect(jsonPath("[0].name")
                        .value("First Programmer"))
                .andExpect(jsonPath("[0].address.zipCode")
                        .value(1000))
                .andExpect(jsonPath("[0].address.city")
                        .value("Budapest"))
                .andExpect(jsonPath("[0].address.street")
                        .value("Test street"))
                .andExpect(jsonPath("[0].birthDate.day")
                        .value(11))
                .andExpect(jsonPath("[0].birthDate.month")
                        .value(3))
                .andExpect(jsonPath("[0].birthDate.year")
                        .value(1999))
                .andExpect(jsonPath("[0].phoneNumber")
                        .value("+36203456789"))
                .andExpect(jsonPath("[0].email")
                        .value("firstprogrammer@gmail.com"))
                .andExpect(jsonPath("[0].isApprentice")
                        .value(true))
                .andExpect(jsonPath("[0].deleted")
                        .value(false));

        mockMvc.perform(get("/api/programmers"))
                .andExpect(jsonPath("[1].name")
                        .value("Second Programmer"));

        Mockito.verify(programmerService, Mockito.times(2))
                .findAllSorted(null, null);
    }

    @Test
    public void apiListProgrammers_WithProgrammers_ReturnsCorrectSize() throws Exception {

        Mockito.when(programmerService.findAllSorted(Mockito.isNull(), Mockito.isNull())).thenReturn(programmers);

        mockMvc.perform(get("/api/programmers"))
                .andExpect(jsonPath("$", hasSize(programmers.size())));

        Mockito.verify(programmerService, Mockito.times(1))
                .findAllSorted(null, null);
    }

    @Test
    public void apiListProgrammers_EmptyList_ReturnsErrorDto() throws Exception {

        var listEmpty = new ArrayList<ProgrammerDto>();
        Mockito.when(programmerService.findAllSorted(Mockito.isNull(), Mockito.isNull())).thenReturn(listEmpty);

        mockMvc.perform(get("/api/programmers"))
                .andExpect(jsonPath("$.error")
                        .value(ProgrammingConstants.noProgrammerFound));

        Mockito.verify(programmerService, Mockito.times(1))
                .findAllSorted(null, null);
    }

    @Test
    public void apiListProgrammers_WithProgrammersWithSorting_ReturnsList() throws Exception {

        Mockito.when(programmerService.findAllSorted("name", "asc")).thenReturn(programmers);

        mockMvc.perform(get("/api/programmers?sortby=name&order=asc"))
                .andExpect(jsonPath("[0].name")
                        .value("First Programmer"))
                .andExpect(jsonPath("[0].address.zipCode")
                        .value(1000))
                .andExpect(jsonPath("[0].address.city")
                        .value("Budapest"))
                .andExpect(jsonPath("[0].address.street")
                        .value("Test street"))
                .andExpect(jsonPath("[0].birthDate.day")
                        .value(11))
                .andExpect(jsonPath("[0].birthDate.month")
                        .value(3))
                .andExpect(jsonPath("[0].birthDate.year")
                        .value(1999))
                .andExpect(jsonPath("[0].phoneNumber")
                        .value("+36203456789"))
                .andExpect(jsonPath("[0].email")
                        .value("firstprogrammer@gmail.com"))
                .andExpect(jsonPath("[0].isApprentice")
                        .value(true))
                .andExpect(jsonPath("[0].deleted")
                        .value(false));

        Mockito.verify(programmerService, Mockito.times(1))
                .findAllSorted("name", "asc");
    }

    @Test
    public void apiDetailsProgrammer_ValidId_ReturnsCorrectProgrammerDetails() throws Exception {

        Mockito.when(programmerService.findById(1L)).thenReturn(modelMapper.map(programmer, ProgrammerDetailsDto.class));

        mockMvc.perform(get("/api/details-programmers/1"))
                .andExpect(jsonPath("$.name")
                        .value("First Programmer"))
                .andExpect(jsonPath("$.address.zipCode")
                        .value(1000))
                .andExpect(jsonPath("$.address.city")
                        .value("Budapest"))
                .andExpect(jsonPath("$.address.street")
                        .value("Test street"))
                .andExpect(jsonPath("$.birthDate.day")
                        .value(11))
                .andExpect(jsonPath("$.birthDate.month")
                        .value(3))
                .andExpect(jsonPath("$.birthDate.year")
                        .value(1999))
                .andExpect(jsonPath("$.phoneNumber")
                        .value("+36203456789"))
                .andExpect(jsonPath("$.email")
                        .value("firstprogrammer@gmail.com"))
                .andExpect(jsonPath("$.project.client")
                        .value("Test Client"))
                .andExpect(jsonPath("$.project.startDate")
                        .value(2162))
                .andExpect(jsonPath("$.project.description")
                        .value("Test project"))
                .andExpect(jsonPath("$.project.deleted")
                        .value(false))
                .andExpect(jsonPath("$.isApprentice")
                        .value(true))
                .andExpect(jsonPath("$.deleted")
                        .value(false));

        Mockito.verify(programmerService, Mockito.times(1))
                .findById(1L);
    }

    @Test
    public void apiDetailsProgrammer_InvalidId_ReturnsErrorDto() throws Exception {

        Mockito.when(programmerService.findById(999L)).thenReturn(null);

        mockMvc.perform(get("/api/details-programmers/999"))
                .andExpect(jsonPath("$.error")
                        .value(ProgrammingConstants.noProgrammerFound));

        Mockito.verify(programmerService, Mockito.times(1))
                .findById(999L);
    }

    @Test
    public void apiDeleteProgrammers_InvalidId_ReturnsErrorDto() throws Exception {

        Mockito.when(programmerService.deleteById(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/delete-programmers/999"))
                .andExpect(jsonPath("$.error")
                        .value(ProgrammingConstants.noProgrammerFound));

        Mockito.verify(programmerService, Mockito.times(1))
                .deleteById(999L);
    }

    @Test
    public void apiDeleteProgrammer_ValidId_ReturnsSuccessDto() throws Exception {

        programmer.setProgrammerId(666L);
        Mockito.when(programmerService.deleteById(programmer.getProgrammerId())).thenReturn(true);
        Mockito.when(programmerService.findById(programmer.getProgrammerId())).thenReturn(modelMapper.map(programmer, ProgrammerDetailsDto.class));

        mockMvc.perform(delete("/api/delete-programmers/" + programmer.getProgrammerId()))
                .andExpect(jsonPath("$.success")
                        .value(ProgrammingConstants.deleteSuccess("programmer")));

        Mockito.verify(programmerService, Mockito.times(1))
                .deleteById(666L);
    }

    @Test
    public void apiAddProgrammer_ValidProgrammer_ReturnsSuccessDto() throws Exception {

        Mockito.when(programmerService.save(Mockito.any(ProgrammerDto.class)))
                .thenReturn(new ValidatorResultDto(true, ProgrammingConstants.saveSuccess("programmer")));

        mockMvc.perform(post("/api/add-programmers")
                        .content("{\n" +
                                "  \"name\": \"Added programmer\",\n" +
                                "  \"address\": {\n" +
                                "    \"zipCode\": 1000,\n" +
                                "    \"city\": \"Budapest\",\n" +
                                "    \"street\": \"Test street\"\n" +
                                "  },\n" +
                                "  \"birthDate\": {\n" +
                                "    \"day\": 11,\n" +
                                "    \"month\": 3,\n" +
                                "    \"year\": 1999\n" +
                                "  },\n" +
                                "  \"phoneNumber\": \"+36303466789\",\n" +
                                "  \"email\": \"test@test.com\",\n" +
                                "  \"responsibility\": \"BACKEND\",\n" +
                                "  \"isApprentice\": true,\n" +
                                "  \"deleted\": \"false\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(ProgrammingConstants.saveSuccess("programmer")));

        Mockito.verify(programmerService, Mockito.times(1))
                .save(Mockito.any(ProgrammerDto.class));
    }

    @Test
    public void apiAddProgrammer_InvalidProgrammer_ReturnsErrorDto() throws Exception {

        Mockito.when(programmerService.save(Mockito.any(ProgrammerDto.class)))
                .thenReturn(new ValidatorResultDto(false, ProgrammingConstants.saveFail("programmer")));

        mockMvc.perform(post("/api/add-programmers")
                        .content("{\n" +
                                "  \"name\": \"Test programmer\",\n" +
                                "  \"address\": {\n" +
                                "    \"zipCode\": 1130,\n" +
                                "    \"city\": \"Budapest\",\n" +
                                "    \"street\": \"Test street\"\n" +
                                "  },\n" +
                                "  \"birthDate\": {\n" +
                                "    \"day\": 111,\n" +
                                "    \"month\": 3,\n" +
                                "    \"year\": 1999\n" +
                                "  },\n" +
                                "  \"phoneNumber\": \"+36303466789\",\n" +
                                "  \"email\": \"test@test.com\",\n" +
                                "  \"responsibility\": \"BACKEND\",\n" +
                                "  \"isApprentice\": true,\n" +
                                "  \"deleted\": \"true\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value(ProgrammingConstants.saveFail("programmer")));

        Mockito.verify(programmerService, Mockito.times(1))
                .save(Mockito.any(ProgrammerDto.class));
    }

    @Test
    public void apiEditProgrammer_ValidProgrammer_ReturnsSuccessDto() throws Exception {

        Mockito.when(programmerService.editById(Mockito.anyLong(), Mockito.any(ProgrammerDto.class)))
                .thenReturn(new ValidatorResultDto(true, ProgrammingConstants.saveSuccess("programmer")));

        mockMvc.perform(post("/api/edit-programmers/1")
                        .content("{\n" +
                                "  \"name\": \"Test programmer\",\n" +
                                "  \"address\": {\n" +
                                "    \"zipCode\": 1130,\n" +
                                "    \"city\": \"Budapest\",\n" +
                                "    \"street\": \"Test street\"\n" +
                                "  },\n" +
                                "  \"birthDate\": {\n" +
                                "    \"day\": 11,\n" +
                                "    \"month\": 3,\n" +
                                "    \"year\": 1999\n" +
                                "  },\n" +
                                "  \"phoneNumber\": \"+36203466789\",\n" +
                                "  \"email\": \"test@test.com\",\n" +
                                "  \"responsibility\": \"BACKEND\",\n" +
                                "  \"isApprentice\": true,\n" +
                                "  \"deleted\": \"false\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(ProgrammingConstants.saveSuccess("programmer")));

        Mockito.verify(programmerService, Mockito.times(1))
                .editById(Mockito.anyLong(), Mockito.any(ProgrammerDto.class));
    }

    @Test
    public void apiEditProgrammer_InvalidProgrammer_ReturnsErrorDto() throws Exception {

        Mockito.when(programmerService.editById(Mockito.anyLong(), Mockito.any(ProgrammerDto.class)))
                .thenReturn(new ValidatorResultDto(false, ProgrammingConstants.saveFail("programmer")));

        mockMvc.perform(post("/api/edit-programmers/1")
                        .content("{\n" +
                                "  \"name\": \"Test programmer\",\n" +
                                "  \"address\": {\n" +
                                "    \"zipCode\": 1130,\n" +
                                "    \"city\": \"Budapest\",\n" +
                                "    \"street\": \"Test street\"\n" +
                                "  },\n" +
                                "  \"birthDate\": {\n" +
                                "    \"day\": 111,\n" +
                                "    \"month\": 3,\n" +
                                "    \"year\": 1999\n" +
                                "  },\n" +
                                "  \"phoneNumber\": \"+36203466789\",\n" +
                                "  \"email\": \"test@test.com\",\n" +
                                "  \"responsibility\": \"BACKEND\",\n" +
                                "  \"isApprentice\": true,\n" +
                                "  \"deleted\": \"false\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value(ProgrammingConstants.saveFail("programmer")));

        Mockito.verify(programmerService, Mockito.times(1))
                .editById(Mockito.anyLong(), Mockito.any(ProgrammerDto.class));
    }
}
