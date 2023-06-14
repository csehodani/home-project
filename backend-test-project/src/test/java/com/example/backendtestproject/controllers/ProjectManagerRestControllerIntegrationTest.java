package com.example.backendtestproject.controllers;

import com.example.backendtestproject.constants.ProgrammingConstants;
import com.example.backendtestproject.models.ProjectManager;
import com.example.backendtestproject.repositories.ProjectManagerRepository;
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
public class ProjectManagerRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectManagerRepository projectManagerRepository;

    @Test
    public void apiListProjectManagers_ListContainsProjectManagers_StatusIsOk() throws Exception {
        mockMvc.perform(get("/api/project-managers"))
                .andExpect(status().isOk());
    }

    @Test
    public void apiListProjectManagers_ListContainsProjectManagers_ReturnsCorrectListSize() throws Exception {
        int numberOfProjectManagers = ((List<ProjectManager>) projectManagerRepository.findAll()).size();

        mockMvc.perform(get("/api/project-managers"))
                .andExpect(jsonPath("$", hasSize(numberOfProjectManagers)));
    }

    @Test
    public void apiListProjectManagers_ListContainsProjectManagers_ReturnsCorrectFields() throws Exception {
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
                        .value("+36303466789"))
                .andExpect(jsonPath("[0].email")
                        .value("firstmanager@gmail.com"))
                .andExpect(jsonPath("[0].deleted")
                        .value(false));
    }

    @Test
    public void apiDetails_ProjectManagerExists_StatusOk() throws Exception {
        mockMvc.perform(get("/api/details-project-managers/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void apiDetails_ProjectManagerExists_ReturnsCorrectFields() throws Exception {
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
                        .value("+36303466789"))
                .andExpect(jsonPath("$.email")
                        .value("firstmanager@gmail.com"))
                .andExpect(jsonPath("$.project.client")
                        .value("First Client"))
                .andExpect(jsonPath("$.project.startDate")
                        .value("2023"))
                .andExpect(jsonPath("$.project.description")
                        .value("First"))
                .andExpect(jsonPath("$.deleted")
                        .value(false));
    }

    @Test
    public void apiListProjectManagers_WithNameOrderingAscending_ReturnsCorrectOrder() throws Exception {
        mockMvc.perform(get("/api/project-managers")
                        .param("sortby", "name")
                        .param("order", "asc"))
                .andExpect(jsonPath("[0].name")
                        .value("Added Manager"))
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
                        .value("+36303466789"))
                .andExpect(jsonPath("[0].email")
                        .value("secondmanager@gmail.com"))
                .andExpect(jsonPath("[0].deleted")
                        .value(false));
    }

    @Test
    public void apiListProjectManagers_WithNonExistingFieldOrderingAscending_ReturnsOriginalProjectManagers() throws Exception {
        mockMvc.perform(get("/api/project-managers")
                        .param("sortby", "nonExistingField")
                        .param("order", "asc"))
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
                        .value("+36303466789"))
                .andExpect(jsonPath("[0].email")
                        .value("firstmanager@gmail.com"))
                .andExpect(jsonPath("[0].deleted")
                        .value(false));
    }

    @Test
    public void apiDeleteProjectManagers_ProjectManagerDoesNotExist_StatusIsBadRequest() throws Exception {
        mockMvc.perform(delete("/api/delete-project-managers/999"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void apiDeleteProjectManagers_ProjectManagerDoesNotExist_ReturnsErrorDto() throws Exception {
        mockMvc.perform(delete("/api/delete-project-managers/999"))
                .andExpect(jsonPath("$.error")
                        .value(ProgrammingConstants.noProjectManagerFound));
    }

    @Test
    public void apiAddDeleteProjectManagers_NewProjectManagerCreatedAndDeleted_StatusOk() throws Exception {
        int numberOfProjectManagers = ((List<ProjectManager>) projectManagerRepository.findAll()).size();

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
                                "  \"email\": \"test61@test.com\",\n" +
                                "  \"deleted\": \"false\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(ProgrammingConstants.saveSuccess("project manager")));

        mockMvc.perform(get("/api/project-managers"))
                .andExpect(jsonPath("$", hasSize(numberOfProjectManagers + 1)));

        ProjectManager lastProjectManager = ((List<ProjectManager>) projectManagerRepository.findAll())
                .stream().max(Comparator.comparing(ProjectManager::getProjectManagerId))
                .orElse(null);

        mockMvc.perform(delete("/api/delete-project-managers/" + lastProjectManager.getProjectManagerId()))
                .andExpect(jsonPath("$.success")
                        .value(ProgrammingConstants.deleteSuccess("project manager")));

        mockMvc.perform(get("/api/project-managers"))
                .andExpect(jsonPath("$", hasSize(numberOfProjectManagers)));
    }

    @Test
    public void apiAddProjectManagers_NewProjectManagerWithBadZipCode_StatusIsBadRequest() throws Exception {
        mockMvc.perform(post("/api/add-project-managers")
                        .content("{\n" +
                                "  \"name\": \"Added manager\",\n" +
                                "  \"address\": {\n" +
                                "    \"zipCode\": 10000,\n" +
                                "    \"city\": \"Budapest\",\n" +
                                "    \"street\": \"Test street\"\n" +
                                "  },\n" +
                                "  \"birthDate\": {\n" +
                                "    \"day\": 11,\n" +
                                "    \"month\": 3,\n" +
                                "    \"year\": 1999\n" +
                                "  },\n" +
                                "  \"phoneNumber\": \"+36303466789\",\n" +
                                "  \"email\": \"test50@test.com\",\n" +
                                "  \"deleted\": \"false\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value(ProgrammingConstants.saveFail("project manager")
                                + ProgrammingConstants.zipCodeInvalid));
    }

    @Test
    public void apiAddProjectManager_NewProjectManagerWithoutName_StatusIsBadRequest() throws Exception {
        mockMvc.perform(post("/api/add-project-managers")
                        .content("{\n" +
                                "  \"name\": \"\",\n" +
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
                                "  \"email\": \"test300@test.com\",\n" +
                                "  \"deleted\": \"false\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value(ProgrammingConstants.saveFail("project manager")
                                + ProgrammingConstants.nameMissing));
    }

    @Test
    public void apiAddProjectManager_NewProjectManagerWithoutCityAndStreet_StatusIsBadRequest() throws Exception {
        mockMvc.perform(post("/api/add-project-managers")
                        .content("{\n" +
                                "  \"name\": \"Added manager\",\n" +
                                "  \"address\": {\n" +
                                "    \"zipCode\": 1000,\n" +
                                "    \"city\": \"\",\n" +
                                "    \"street\": \"\"\n" +
                                "  },\n" +
                                "  \"birthDate\": {\n" +
                                "    \"day\": 11,\n" +
                                "    \"month\": 3,\n" +
                                "    \"year\": 1999\n" +
                                "  },\n" +
                                "  \"phoneNumber\": \"+36303466789\",\n" +
                                "  \"email\": \"test44@test.com\",\n" +
                                "  \"deleted\": \"false\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value(ProgrammingConstants.saveFail("project manager")
                                + ProgrammingConstants.cityMissing
                                + ProgrammingConstants.streetMissing));
    }

    @Test
    public void apiEditProjectManager_NewProjectManagerCreatedAndEdited_StatusOk() throws Exception {

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
                                "  \"email\": \"test120@test.com\",\n" +
                                "  \"deleted\": \"false\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(ProgrammingConstants.saveSuccess("project manager")));

        ProjectManager lastProjectManager = ((List<ProjectManager>) projectManagerRepository.findAll())
                .stream().max(Comparator.comparing(ProjectManager::getProjectManagerId)).orElse(null);

        mockMvc.perform(post("/api/edit-project-managers/" + lastProjectManager.getProjectManagerId())
                        .content("{\n" +
                                "  \"name\": \"Edited manager\",\n" +
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
                                "  \"email\": \"test113@test.com\",\n" +
                                "  \"deleted\": \"false\"\n" +
                                "}").contentType(MediaType.APPLICATION_JSON)).andExpect(status()
                        .isOk())
                .andExpect(jsonPath("$.success").value(ProgrammingConstants.saveSuccess("project manager")));

        int numberOfProjectManagers = ((List<ProjectManager>) projectManagerRepository.findAll()).size() - 1;

        mockMvc.perform(get("/api/project-managers"))
                .andExpect(jsonPath("[" + numberOfProjectManagers + "].name")
                        .value("Edited manager"))
                .andExpect(jsonPath("[" + numberOfProjectManagers + "].address.zipCode")
                        .value(1000))
                .andExpect(jsonPath("[" + numberOfProjectManagers + "].address.city")
                        .value("Budapest"))
                .andExpect(jsonPath("[" + numberOfProjectManagers + "].address.street")
                        .value("Test street"))
                .andExpect(jsonPath("[" + numberOfProjectManagers + "].birthDate.day")
                        .value(11))
                .andExpect(jsonPath("[" + numberOfProjectManagers + "].birthDate.month")
                        .value("3"))
                .andExpect(jsonPath("[" + numberOfProjectManagers + "].birthDate.year")
                        .value("1999"))
                .andExpect(jsonPath("[" + numberOfProjectManagers + "].phoneNumber")
                        .value("+36303466789"))
                .andExpect(jsonPath("[" + numberOfProjectManagers + "].email")
                        .value("test113@test.com"))
                .andExpect(jsonPath("[" + numberOfProjectManagers + "].deleted")
                        .value(false));

        mockMvc.perform(delete("/api/delete-project-managers/" + lastProjectManager.getProjectManagerId()))
                .andExpect(jsonPath("$.success")
                        .value(ProgrammingConstants.deleteSuccess("project manager")));
    }
}
