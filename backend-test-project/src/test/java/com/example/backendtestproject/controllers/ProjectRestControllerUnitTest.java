package com.example.backendtestproject.controllers;

import com.example.backendtestproject.constants.ProgrammingConstants;
import com.example.backendtestproject.dtos.ProjectDto;
import com.example.backendtestproject.dtos.ValidatorResultDto;
import com.example.backendtestproject.models.Project;
import com.example.backendtestproject.services.ProjectService;
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

@WebMvcTest(ProjectRestController.class)
@AutoConfigureMockMvc
public class ProjectRestControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    private ModelMapper modelMapper;

    private Project project;

    private List projects;

    @BeforeEach
    public void init() {
        modelMapper = new ModelMapper();
        projects = List.of(
                Project.builder()
                        .client("First Client")
                        .startDate("2023")
                        .description("First")
                        .deleted(false).build(),

                Project.builder()
                        .client("Second Client")
                        .startDate("2024")
                        .description("Second")
                        .deleted(false).build());

        project = Project.builder()
                .client("First Client")
                .startDate("2023")
                .description("First")
                .deleted(false).build();

    }

    @Test
    public void apiListProjects_WithProjects_ReturnsList() throws Exception {

        Mockito.when(projectService.findAllSorted(Mockito.isNull(), Mockito.isNull())).thenReturn(projects);

        mockMvc.perform(get("/api/projects"))
                .andExpect(jsonPath("[0].client")
                        .value("First Client"))
                .andExpect(jsonPath("[0].startDate")
                        .value("2023"))
                .andExpect(jsonPath("[0].description")
                        .value("First"))
                .andExpect(jsonPath("[0].deleted")
                        .value(false));

        mockMvc.perform(get("/api/projects"))
                .andExpect(jsonPath("[1].client")
                        .value("Second Client"));

        Mockito.verify(projectService, Mockito.times(2))
                .findAllSorted(null, null);
    }

    @Test
    public void apiListProjects_WithProjects_ReturnsCorrectSize() throws Exception {

        Mockito.when(projectService.findAllSorted(Mockito.isNull(), Mockito.isNull())).thenReturn(projects);

        mockMvc.perform(get("/api/projects"))
                .andExpect(jsonPath("$", hasSize(projects.size())));

        Mockito.verify(projectService, Mockito.times(1))
                .findAllSorted(null, null);
    }

    @Test
    public void apiListProjects_EmptyList_ReturnsErrorDto() throws Exception {

        var listEmpty = new ArrayList<ProjectDto>();
        Mockito.when(projectService.findAllSorted(Mockito.isNull(), Mockito.isNull())).thenReturn(listEmpty);

        mockMvc.perform(get("/api/projects"))
                .andExpect(jsonPath("$.error")
                        .value(ProgrammingConstants.noProjectFound));

        Mockito.verify(projectService, Mockito.times(1))
                .findAllSorted(null, null);
    }

    @Test
    public void apiListProjects_WithProjectsWithSorting_ReturnsList() throws Exception {

        Mockito.when(projectService.findAllSorted("client", "asc")).thenReturn(projects);

        mockMvc.perform(get("/api/projects?sortby=client&order=asc"))
                .andExpect(jsonPath("[0].client")
                        .value("First Client"))
                .andExpect(jsonPath("[0].startDate")
                        .value("2023"))
                .andExpect(jsonPath("[0].description")
                        .value("First"))
                .andExpect(jsonPath("[0].deleted")
                        .value(false));

        Mockito.verify(projectService, Mockito.times(1))
                .findAllSorted("client", "asc");
    }

    @Test
    public void apiDetailsProject_ValidId_ReturnsCorrectProjectDetails() throws Exception {

        Mockito.when(projectService.findById(1L)).thenReturn(modelMapper.map(project, ProjectDto.class));

        mockMvc.perform(get("/api/details-projects/1"))
                .andExpect(jsonPath("$.client")
                        .value("First Client"))
                .andExpect(jsonPath("$.startDate")
                        .value("2023"))
                .andExpect(jsonPath("$.description")
                        .value("First"))
                .andExpect(jsonPath("$.deleted")
                        .value(false));

        Mockito.verify(projectService, Mockito.times(1))
                .findById(1L);
    }

    @Test
    public void apiDetailsProject_InvalidId_ReturnsErrorDto() throws Exception {

        Mockito.when(projectService.findById(999L)).thenReturn(null);

        mockMvc.perform(get("/api/details-projects/999"))
                .andExpect(jsonPath("$.error")
                        .value(ProgrammingConstants.noProjectFound));

        Mockito.verify(projectService, Mockito.times(1))
                .findById(999L);
    }

    @Test
    public void apiDeleteProjects_InvalidId_ReturnsErrorDto() throws Exception {

        Mockito.when(projectService.deleteById(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/delete-projects/999"))
                .andExpect(jsonPath("$.error")
                        .value(ProgrammingConstants.noProjectFound));

        Mockito.verify(projectService, Mockito.times(1))
                .deleteById(999L);
    }

    @Test
    public void apiDeleteProject_ValidId_ReturnsSuccessDto() throws Exception {

        project.setProjectId(666L);
        Mockito.when(projectService.deleteById(project.getProjectId())).thenReturn(true);
        Mockito.when(projectService.findById(project.getProjectId())).thenReturn(modelMapper.map(project, ProjectDto.class));

        mockMvc.perform(delete("/api/delete-projects/" + project.getProjectId()))
                .andExpect(jsonPath("$.success")
                        .value(ProgrammingConstants.deleteSuccess("project")));

        Mockito.verify(projectService, Mockito.times(1))
                .deleteById(666L);
    }

    @Test
    public void apiAddProjects_ValidProject_ReturnsSuccessDto() throws Exception {

        Mockito.when(projectService.save(Mockito.any(ProjectDto.class)))
                .thenReturn(new ValidatorResultDto(true, ProgrammingConstants.saveSuccess("project")));

        mockMvc.perform(post("/api/add-projects")
                        .content("{\n" +
                                "  \"client\": \"Added client\",\n" +
                                "  \"startDate\": \"2023\",\n" +
                                "  \"description\": \"added\",\n" +
                                "  \"deleted\": \"false\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(ProgrammingConstants.saveSuccess("project")));

        Mockito.verify(projectService, Mockito.times(1))
                .save(Mockito.any(ProjectDto.class));
    }

    @Test
    public void apiAddProject_InvalidProject_ReturnsErrorDto() throws Exception {

        Mockito.when(projectService.save(Mockito.any(ProjectDto.class)))
                .thenReturn(new ValidatorResultDto(false, ProgrammingConstants.saveFail("project")));

        mockMvc.perform(post("/api/add-projects")
                        .content("{\n" +
                                "  \"client\": \"Test client\",\n" +
                                "  \"startDate\": \"2023\",\n" +
                                "  \"deleted\": \"true\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value(ProgrammingConstants.saveFail("project")));

        Mockito.verify(projectService, Mockito.times(1))
                .save(Mockito.any(ProjectDto.class));
    }

    @Test
    public void apiEditProject_ValidProject_ReturnsSuccessDto() throws Exception {

        Mockito.when(projectService.editById(Mockito.anyLong(), Mockito.any(ProjectDto.class)))
                .thenReturn(new ValidatorResultDto(true, ProgrammingConstants.saveSuccess("project")));

        mockMvc.perform(post("/api/edit-projects/1")
                        .content("{\n" +
                                "  \"client\": \"Test Client\",\n" +
                                "  \"startDate\": \"2025\",\n" +
                                "  \"description\": \"description\",\n" +
                                "  \"deleted\": \"false\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(ProgrammingConstants.saveSuccess("project")));

        Mockito.verify(projectService, Mockito.times(1))
                .editById(Mockito.anyLong(), Mockito.any(ProjectDto.class));
    }

    @Test
    public void apiEditProject_InvalidProject_ReturnsErrorDto() throws Exception {

        Mockito.when(projectService.editById(Mockito.anyLong(), Mockito.any(ProjectDto.class)))
                .thenReturn(new ValidatorResultDto(false, ProgrammingConstants.saveFail("project")));

        mockMvc.perform(post("/api/edit-projects/1")
                        .content("{\n" +
                                "  \"client\": \"Test client\",\n" +
                                "  \"description\": \"description\",\n" +
                                "  \"deleted\": \"false\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value(ProgrammingConstants.saveFail("project")));

        Mockito.verify(projectService, Mockito.times(1))
                .editById(Mockito.anyLong(), Mockito.any(ProjectDto.class));
    }
}
