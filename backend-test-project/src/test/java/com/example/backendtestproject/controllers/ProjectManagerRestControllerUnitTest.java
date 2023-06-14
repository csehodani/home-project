package com.example.backendtestproject.controllers;

import com.example.backendtestproject.constants.ProgrammingConstants;
import com.example.backendtestproject.dtos.ProjectManagerDetailsDto;
import com.example.backendtestproject.dtos.ProjectManagerDto;
import com.example.backendtestproject.dtos.ValidatorResultDto;
import com.example.backendtestproject.models.Address;
import com.example.backendtestproject.models.BirthDate;
import com.example.backendtestproject.models.Project;
import com.example.backendtestproject.models.ProjectManager;
import com.example.backendtestproject.services.ProjectManagerService;
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

@WebMvcTest(ProjectManagerRestController.class)
@AutoConfigureMockMvc
public class ProjectManagerRestControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectManagerService projectManagerService;

    private ModelMapper modelMapper;

    private ProjectManager projectManager;

    private List projectManagers;

    @BeforeEach
    public void init() {
        modelMapper = new ModelMapper();
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

    }

    @Test
    public void apiListProjectManagers_WithProjectManagers_ReturnsList() throws Exception {

        Mockito.when(projectManagerService.findAllSorted(Mockito.isNull(), Mockito.isNull())).thenReturn(projectManagers);

        mockMvc.perform(get("/api/project-managers"))
                .andExpect(jsonPath("[0].name")
                        .value("First Manager"))
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
                        .value("firstmanager@gmail.com"))
                .andExpect(jsonPath("[0].deleted")
                        .value(false));

        mockMvc.perform(get("/api/project-managers"))
                .andExpect(jsonPath("[1].name")
                        .value("Second Manager"));

        Mockito.verify(projectManagerService, Mockito.times(2))
                .findAllSorted(null, null);
    }

    @Test
    public void apiListProjectManagers_WithProjectManagers_ReturnsCorrectSize() throws Exception {

        Mockito.when(projectManagerService.findAllSorted(Mockito.isNull(), Mockito.isNull())).thenReturn(projectManagers);

        mockMvc.perform(get("/api/project-managers"))
                .andExpect(jsonPath("$", hasSize(projectManagers.size())));

        Mockito.verify(projectManagerService, Mockito.times(1))
                .findAllSorted(null, null);
    }

    @Test
    public void apiListProjectManagers_EmptyList_ReturnsErrorDto() throws Exception {

        var listEmpty = new ArrayList<ProjectManagerDto>();
        Mockito.when(projectManagerService.findAllSorted(Mockito.isNull(), Mockito.isNull())).thenReturn(listEmpty);

        mockMvc.perform(get("/api/project-managers"))
                .andExpect(jsonPath("$.error")
                        .value(ProgrammingConstants.noProjectManagerFound));

        Mockito.verify(projectManagerService, Mockito.times(1))
                .findAllSorted(null, null);
    }

    @Test
    public void apiListProjectManagers_WithProjectManagersWithSorting_ReturnsList() throws Exception {

        Mockito.when(projectManagerService.findAllSorted("name", "asc")).thenReturn(projectManagers);

        mockMvc.perform(get("/api/project-managers?sortby=name&order=asc"))
                .andExpect(jsonPath("[0].name")
                        .value("First Manager"))
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
                        .value("firstmanager@gmail.com"))
                .andExpect(jsonPath("[0].deleted")
                        .value(false));

        Mockito.verify(projectManagerService, Mockito.times(1))
                .findAllSorted("name", "asc");
    }

    @Test
    public void apiDetailsProjectManager_ValidId_ReturnsCorrectProjectManagerDetails() throws Exception {

        Mockito.when(projectManagerService.findById(1L)).thenReturn(modelMapper.map(projectManager, ProjectManagerDetailsDto.class));

        mockMvc.perform(get("/api/details-project-managers/1"))
                .andExpect(jsonPath("$.name")
                        .value("First Manager"))
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
                        .value("firstmanager@gmail.com"))
                .andExpect(jsonPath("$.project.client")
                        .value("Test Client"))
                .andExpect(jsonPath("$.project.startDate")
                        .value(2162))
                .andExpect(jsonPath("$.project.description")
                        .value("Test project"))
                .andExpect(jsonPath("$.deleted")
                        .value(false));

        Mockito.verify(projectManagerService, Mockito.times(1))
                .findById(1L);
    }

    @Test
    public void apiDetailsProjectManager_InvalidId_ReturnsErrorDto() throws Exception {

        Mockito.when(projectManagerService.findById(999L)).thenReturn(null);

        mockMvc.perform(get("/api/details-project-managers/999"))
                .andExpect(jsonPath("$.error")
                        .value(ProgrammingConstants.noProjectManagerFound));

        Mockito.verify(projectManagerService, Mockito.times(1))
                .findById(999L);
    }

    @Test
    public void apiDeleteProjectManagers_InvalidId_ReturnsErrorDto() throws Exception {

        Mockito.when(projectManagerService.deleteById(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/delete-project-managers/999"))
                .andExpect(jsonPath("$.error")
                        .value(ProgrammingConstants.noProjectManagerFound));

        Mockito.verify(projectManagerService, Mockito.times(1))
                .deleteById(999L);
    }

    @Test
    public void apiDeleteProjectManager_ValidId_ReturnsSuccessDto() throws Exception {

        projectManager.setProjectManagerId(666L);
        Mockito.when(projectManagerService.deleteById(projectManager.getProjectManagerId())).thenReturn(true);
        Mockito.when(projectManagerService.findById(projectManager.getProjectManagerId())).thenReturn(modelMapper.map(projectManager, ProjectManagerDetailsDto.class));

        mockMvc.perform(delete("/api/delete-project-managers/" + projectManager.getProjectManagerId()))
                .andExpect(jsonPath("$.success")
                        .value(ProgrammingConstants.deleteSuccess("project manager")));

        Mockito.verify(projectManagerService, Mockito.times(1))
                .deleteById(666L);
    }

    @Test
    public void apiAddProjectManagers_ValidProjectManager_ReturnsSuccessDto() throws Exception {

        Mockito.when(projectManagerService.save(Mockito.any(ProjectManagerDto.class)))
                .thenReturn(new ValidatorResultDto(true, ProgrammingConstants.saveSuccess("project manager")));

        mockMvc.perform(post("/api/add-project-managers")
                        .content("{\n" +
                                "  \"name\": \"Added manager\",\n" +
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
                                "  \"deleted\": \"false\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(ProgrammingConstants.saveSuccess("project manager")));

        Mockito.verify(projectManagerService, Mockito.times(1))
                .save(Mockito.any(ProjectManagerDto.class));
    }

    @Test
    public void apiAddProjectManager_InvalidProjectManager_ReturnsErrorDto() throws Exception {

        Mockito.when(projectManagerService.save(Mockito.any(ProjectManagerDto.class)))
                .thenReturn(new ValidatorResultDto(false, ProgrammingConstants.saveFail("project manager")));

        mockMvc.perform(post("/api/add-project-managers")
                        .content("{\n" +
                                "  \"name\": \"Test manager\",\n" +
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
                                "  \"deleted\": \"true\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value(ProgrammingConstants.saveFail("project manager")));

        Mockito.verify(projectManagerService, Mockito.times(1))
                .save(Mockito.any(ProjectManagerDto.class));
    }

    @Test
    public void apiEditProjectManager_ValidProjectManager_ReturnsSuccessDto() throws Exception {

        Mockito.when(projectManagerService.editById(Mockito.anyLong(), Mockito.any(ProjectManagerDto.class)))
                .thenReturn(new ValidatorResultDto(true, ProgrammingConstants.saveSuccess("project manager")));

        mockMvc.perform(post("/api/edit-project-managers/1")
                        .content("{\n" +
                                "  \"name\": \"Test manager\",\n" +
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
                                "  \"email\": \"jondoe@gmail.com\",\n" +
                                "  \"deleted\": \"false\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(ProgrammingConstants.saveSuccess("project manager")));

        Mockito.verify(projectManagerService, Mockito.times(1))
                .editById(Mockito.anyLong(), Mockito.any(ProjectManagerDto.class));
    }

    @Test
    public void apiEditProjectManager_InvalidProjectManager_ReturnsErrorDto() throws Exception {

        Mockito.when(projectManagerService.editById(Mockito.anyLong(), Mockito.any(ProjectManagerDto.class)))
                .thenReturn(new ValidatorResultDto(false, ProgrammingConstants.saveFail("project manager")));

        mockMvc.perform(post("/api/edit-project-managers/1")
                        .content("{\n" +
                                "  \"name\": \"Test manager\",\n" +
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
                                "  \"email\": \"jondoe@gmail.com\",\n" +
                                "  \"deleted\": \"false\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value(ProgrammingConstants.saveFail("project manager")));

        Mockito.verify(projectManagerService, Mockito.times(1))
                .editById(Mockito.anyLong(), Mockito.any(ProjectManagerDto.class));
    }
}
