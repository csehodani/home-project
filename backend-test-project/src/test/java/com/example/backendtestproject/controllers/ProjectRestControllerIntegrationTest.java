package com.example.backendtestproject.controllers;

import com.example.backendtestproject.constants.ProgrammingConstants;
import com.example.backendtestproject.models.Project;
import com.example.backendtestproject.repositories.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Comparator;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles({"test"})
public class ProjectRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void apiListProjects_ListContainsProjects_StatusIsOk() throws Exception {
        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk());
    }

    @Test
    public void apiListProjects_ListContainsProjects_ReturnsCorrectListSize() throws Exception {
        int numberOfProjects = ((List<Project>) projectRepository.findAll()).size();

        mockMvc.perform(get("/api/projects"))
                .andExpect(jsonPath("$", hasSize(numberOfProjects)));
    }

    @Test
    public void apiListProjects_ListContainsProjects_ReturnsCorrectFields() throws Exception {
        mockMvc.perform(get("/api/projects"))
                .andExpect(jsonPath("[0].client")
                        .value("First Client"))
                .andExpect(jsonPath("[0].startDate")
                        .value("2023"))
                .andExpect(jsonPath("[0].description")
                        .value("First"))
                .andExpect(jsonPath("[0].deleted")
                        .value(false));
    }

    @Test
    public void apiDetails_ProjectExists_StatusOk() throws Exception {
        mockMvc.perform(get("/api/details-projects/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void apiDetails_ProjectExists_ReturnsCorrectFields() throws Exception {
        mockMvc.perform(get("/api/details-projects/1"))
                .andExpect(jsonPath("$.client")
                        .value("First Client"))
                .andExpect(jsonPath("$.startDate")
                        .value("2023"))
                .andExpect(jsonPath("$.description")
                        .value("First"))
                .andExpect(jsonPath("$.deleted")
                        .value(false));
    }

    @Test
    public void apiListProjects_WithNameOrderingAscending_ReturnsCorrectOrder() throws Exception {
        mockMvc.perform(get("/api/projects")
                        .param("sortby", "client")
                        .param("order", "asc"))
                .andExpect(jsonPath("[0].client")
                        .value("Edited Client"))
                .andExpect(jsonPath("[0].startDate")
                        .value("1/3/2023"))
                .andExpect(jsonPath("[0].description")
                        .value("Edited"))
                .andExpect(jsonPath("[0].deleted")
                        .value(false));
    }

    @Test
    public void apiListProjects_WithNonExistingFieldOrderingAscending_ReturnsOriginalProjects() throws Exception {
        mockMvc.perform(get("/api/projects")
                        .param("sortby", "nonExistingField")
                        .param("order", "asc"))
                .andExpect(jsonPath("[0].client")
                        .value("First Client"))
                .andExpect(jsonPath("[0].startDate")
                        .value("2023"))
                .andExpect(jsonPath("[0].description")
                        .value("First"))
                .andExpect(jsonPath("[0].deleted")
                        .value(false));
    }

    @Test
    public void apiDeleteProjects_ProjectDoesNotExist_StatusIsBadRequest() throws Exception {
        mockMvc.perform(delete("/api/delete-projects/999"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void apiDeleteProjects_ProjectDoesNotExist_ReturnsErrorDto() throws Exception {
        mockMvc.perform(delete("/api/delete-projects/999"))
                .andExpect(jsonPath("$.error")
                        .value(ProgrammingConstants.noProjectFound));
    }

    @Test
    public void apiAddDeleteProjects_NewProjectCreatedAndDeleted_StatusOk() throws Exception {
        int numberOfProjects = ((List<Project>) projectRepository.findAll()).size();

        mockMvc.perform(post("/api/add-projects")
                        .content("{\n" +
                                "  \"client\": \"Added Client\",\n" +
                                "  \"startDate\": \"1/3/2023\",\n" +
                                "  \"description\": \"Added\",\n" +
                                "  \"deleted\": \"false\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(ProgrammingConstants.saveSuccess("project")));

        mockMvc.perform(get("/api/projects"))
                .andExpect(jsonPath("$", hasSize(numberOfProjects + 1)));

        Project lastProject = ((List<Project>) projectRepository.findAll())
                .stream().max(Comparator.comparing(Project::getProjectId))
                .orElse(null);

        mockMvc.perform(delete("/api/delete-projects/" + lastProject.getProjectId()))
                .andExpect(jsonPath("$.success")
                        .value(ProgrammingConstants.deleteSuccess("project")));

        mockMvc.perform(get("/api/projects"))
                .andExpect(jsonPath("$", hasSize(numberOfProjects)));
    }

    @Test
    public void apiAddProject_NewProjectWithoutClient_StatusIsBadRequest() throws Exception {
        mockMvc.perform(post("/api/add-projects")
                        .content("{\n" +
                                "  \"client\": \"\",\n" +
                                "  \"startDate\": \"1/3/2023\",\n" +
                                "  \"description\": \"New\",\n" +
                                "  \"deleted\": \"false\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value(ProgrammingConstants.saveFail("project")
                                + ProgrammingConstants.clientMissing));
    }

    @Test
    public void apiAddProject_NewProjectWithoutStartDate_StatusIsBadRequest() throws Exception {
        mockMvc.perform(post("/api/add-projects")
                        .content("{\n" +
                                "  \"client\": \"Added Client\",\n" +
                                "  \"description\": \"Added\",\n" +
                                "  \"deleted\": \"false\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value(ProgrammingConstants.saveFail("project")
                                + ProgrammingConstants.startDateMissing));
    }

    @Test
    public void apiAddProject_NewProjectWithoutDescription_StatusIsBadRequest() throws Exception {
        mockMvc.perform(post("/api/add-projects")
                        .content("{\n" +
                                "  \"client\": \"Added Client\",\n" +
                                "  \"startDate\": \"1/3/2023\",\n" +
                                "  \"description\": \"\",\n" +
                                "  \"deleted\": \"false\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value(ProgrammingConstants.saveFail("project")
                                + ProgrammingConstants.descriptionMissing));
    }

    @Test
    public void apiEditProject_NewProjectCreatedAndEdited_StatusOk() throws Exception {

        mockMvc.perform(post("/api/add-projects")
                        .content("{\n" +
                                "  \"client\": \"Added Client\",\n" +
                                "  \"startDate\": \"1/3/2023\",\n" +
                                "  \"description\": \"Added\",\n" +
                                "  \"deleted\": \"false\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(ProgrammingConstants.saveSuccess("project")));

        Project lastProject = ((List<Project>) projectRepository.findAll())
                .stream().max(Comparator.comparing(Project::getProjectId)).orElse(null);

        mockMvc.perform(post("/api/edit-projects/" + lastProject.getProjectId())
                        .content("{\n" +
                                "  \"client\": \"Edited Client\",\n" +
                                "  \"startDate\": \"1/3/2023\",\n" +
                                "  \"description\": \"Edited\",\n" +
                                "  \"deleted\": \"false\"\n" +
                                "}").contentType(MediaType.APPLICATION_JSON)).andExpect(status()
                        .isOk())
                .andExpect(jsonPath("$.success").value(ProgrammingConstants.saveSuccess("project")));

        int numberOfProjects = ((List<Project>) projectRepository.findAll()).size() - 1;

        mockMvc.perform(get("/api/projects"))
                .andExpect(jsonPath("[" + numberOfProjects + "].client")
                        .value("Edited Client"))
                .andExpect(jsonPath("[" + numberOfProjects + "].startDate")
                        .value("1/3/2023"))
                .andExpect(jsonPath("[" + numberOfProjects + "].description")
                        .value("Edited"))
                .andExpect(jsonPath("[" + numberOfProjects + "].deleted")
                        .value(false));

        mockMvc.perform(delete("/api/delete-projects/" + lastProject.getProjectId()))
                .andExpect(jsonPath("$.success")
                        .value(ProgrammingConstants.deleteSuccess("project")));
    }
}
