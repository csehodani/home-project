package com.example.backendtestproject.controllers;

import com.example.backendtestproject.constants.ProgrammingConstants;
import com.example.backendtestproject.models.Programmer;
import com.example.backendtestproject.repositories.ProgrammerRepository;
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
public class ProgrammerRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProgrammerRepository programmerRepository;

    @Test
    public void apiListProgrammers_ListContainsProgrammers_StatusIsOk() throws Exception {
        mockMvc.perform(get("/api/programmers"))
                .andExpect(status().isOk());
    }

    @Test
    public void apiListProgrammers_ListContainsProgrammers_ReturnsCorrectListSize() throws Exception {
        int numberOfProgrammers = ((List<Programmer>) programmerRepository.findAll()).size();

        mockMvc.perform(get("/api/programmers"))
                .andExpect(jsonPath("$", hasSize(numberOfProgrammers)));
    }

    @Test
    public void apiListProgrammers_ListContainsProgrammers_ReturnsCorrectFields() throws Exception {
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
                        .value("+36303466789"))
                .andExpect(jsonPath("[0].email")
                        .value("first@first.com"))
                .andExpect(jsonPath("[0].deleted")
                        .value(false));
    }

    @Test
    public void apiDetails_ProgrammerExists_StatusOk() throws Exception {
        mockMvc.perform(get("/api/details-programmers/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void apiDetails_ProgrammerExists_ReturnsCorrectFields() throws Exception {
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
                        .value("+36303466789"))
                .andExpect(jsonPath("$.email")
                        .value("first@first.com"))
                .andExpect(jsonPath("$.project.client")
                        .value("First Client"))
                .andExpect(jsonPath("$.project.startDate")
                        .value("2023"))
                .andExpect(jsonPath("$.project.description")
                        .value("First"))
                .andExpect(jsonPath("$.responsibility")
                        .value("BACKEND"))
                .andExpect(jsonPath("$.isApprentice")
                        .value(true))
                .andExpect(jsonPath("$.deleted")
                        .value(false));
    }

    @Test
    public void apiListProgrammers_WithNameOrderingAscending_ReturnsCorrectOrder() throws Exception {
        mockMvc.perform(get("/api/programmers")
                        .param("sortby", "name")
                        .param("order", "asc"))
                .andExpect(jsonPath("[0].name")
                        .value("Added Programmer"))
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
                        .value("added@added.com"))
                .andExpect(jsonPath("[0].deleted")
                        .value(false));
    }

    @Test
    public void apiListProgrammers_WithNonExistingFieldOrderingAscending_ReturnsOriginalProgrammers() throws Exception {
        mockMvc.perform(get("/api/programmers")
                        .param("sortby", "nonExistingField")
                        .param("order", "asc"))
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
                        .value("+36303466789"))
                .andExpect(jsonPath("[0].email")
                        .value("first@first.com"))
                .andExpect(jsonPath("[0].deleted")
                        .value(false));
    }

    @Test
    public void apiDeleteProgrammers_ProgrammerDoesNotExist_StatusIsBadRequest() throws Exception {
        mockMvc.perform(delete("/api/delete-programmers/999"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void apiDeleteProgrammers_ProgrammerDoesNotExist_ReturnsErrorDto() throws Exception {
        mockMvc.perform(delete("/api/delete-programmers/999"))
                .andExpect(jsonPath("$.error")
                        .value(ProgrammingConstants.noProgrammerFound));
    }

    @Test
    public void apiAddDeleteProgrammers_NewProgrammerCreatedAndDeleted_StatusOk() throws Exception {
        int numberOfProgrammers = ((List<Programmer>) programmerRepository.findAll()).size();

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
                                "  \"email\": \"test610@test.com\",\n" +
                                "  \"responsibility\": \"BACKEND\",\n" +
                                "  \"isApprentice\": true,\n" +
                                "  \"deleted\": \"false\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(ProgrammingConstants.saveSuccess("programmer")));

        mockMvc.perform(get("/api/programmers"))
                .andExpect(jsonPath("$", hasSize(numberOfProgrammers + 1)));

        Programmer lastProgrammer = ((List<Programmer>) programmerRepository.findAll())
                .stream().max(Comparator.comparing(Programmer::getProgrammerId))
                .orElse(null);

        mockMvc.perform(delete("/api/delete-programmers/" + lastProgrammer.getProgrammerId()))
                .andExpect(jsonPath("$.success")
                        .value(ProgrammingConstants.deleteSuccess("programmer")));

        mockMvc.perform(get("/api/programmers"))
                .andExpect(jsonPath("$", hasSize(numberOfProgrammers)));
    }

    @Test
    public void apiAddProgrammers_NewProgrammerWithBadZipCode_StatusIsBadRequest() throws Exception {
        mockMvc.perform(post("/api/add-programmers")
                        .content("{\n" +
                                "  \"name\": \"Added Programmer\",\n" +
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
                                "  \"email\": \"test508@test.com\",\n" +
                                "  \"responsibility\": \"BACKEND\",\n" +
                                "  \"isApprentice\": true,\n" +
                                "  \"deleted\": \"false\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value(ProgrammingConstants.saveFail("programmer")
                                + ProgrammingConstants.zipCodeInvalid));
    }

    @Test
    public void apiAddProgrammer_NewProgrammerWithoutName_StatusIsBadRequest() throws Exception {
        mockMvc.perform(post("/api/add-programmers")
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
                                "  \"responsibility\": \"BACKEND\",\n" +
                                "  \"isApprentice\": true,\n" +
                                "  \"deleted\": \"false\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value(ProgrammingConstants.saveFail("programmer")
                                + ProgrammingConstants.nameMissing));
    }

    @Test
    public void apiAddProgrammer_NewProgrammerWithoutCityAndStreet_StatusIsBadRequest() throws Exception {
        mockMvc.perform(post("/api/add-programmers")
                        .content("{\n" +
                                "  \"name\": \"Added Programmer\",\n" +
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
                                "  \"responsibility\": \"BACKEND\",\n" +
                                "  \"isApprentice\": true,\n" +
                                "  \"deleted\": \"false\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value(ProgrammingConstants.saveFail("programmer")
                                + ProgrammingConstants.cityMissing
                                + ProgrammingConstants.streetMissing));
    }

    @Test
    public void apiEditProgrammer_NewProgrammerCreatedAndEdited_StatusOk() throws Exception {

        mockMvc.perform(post("/api/add-programmers")
                        .content("{\n" +
                                "  \"name\": \"Added Programmer\",\n" +
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
                                "  \"responsibility\": \"BACKEND\",\n" +
                                "  \"isApprentice\": true,\n" +
                                "  \"email\": \"test12002@test.com\",\n" +
                                "  \"deleted\": \"false\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(ProgrammingConstants.saveSuccess("programmer")));

        Programmer lastProgrammer = ((List<Programmer>) programmerRepository.findAll())
                .stream().max(Comparator.comparing(Programmer::getProgrammerId)).orElse(null);

        mockMvc.perform(post("/api/edit-programmers/" + lastProgrammer.getProgrammerId())
                        .content("{\n" +
                                "  \"name\": \"Edited Programmer\",\n" +
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
                                "  \"email\": \"test11372@test.com\",\n" +
                                "  \"responsibility\": \"BACKEND\",\n" +
                                "  \"isApprentice\": true,\n" +
                                "  \"deleted\": \"false\"\n" +
                                "}").contentType(MediaType.APPLICATION_JSON)).andExpect(status()
                        .isOk())
                .andExpect(jsonPath("$.success").value(ProgrammingConstants.saveSuccess("programmer")));

        int numberOfProgrammers = ((List<Programmer>) programmerRepository.findAll()).size() - 1;

        mockMvc.perform(get("/api/programmers"))
                .andExpect(jsonPath("[" + numberOfProgrammers + "].name")
                        .value("Edited Programmer"))
                .andExpect(jsonPath("[" + numberOfProgrammers + "].address.zipCode")
                        .value(1000))
                .andExpect(jsonPath("[" + numberOfProgrammers + "].address.city")
                        .value("Budapest"))
                .andExpect(jsonPath("[" + numberOfProgrammers + "].address.street")
                        .value("Test street"))
                .andExpect(jsonPath("[" + numberOfProgrammers + "].birthDate.day")
                        .value(11))
                .andExpect(jsonPath("[" + numberOfProgrammers + "].birthDate.month")
                        .value("3"))
                .andExpect(jsonPath("[" + numberOfProgrammers + "].birthDate.year")
                        .value("1999"))
                .andExpect(jsonPath("[" + numberOfProgrammers + "].phoneNumber")
                        .value("+36303466789"))
                .andExpect(jsonPath("[" + numberOfProgrammers + "].email")
                        .value("test11372@test.com"))
                .andExpect(jsonPath("[" + numberOfProgrammers + "].responsibility")
                        .value("BACKEND"))
                .andExpect(jsonPath("[" + numberOfProgrammers + "].isApprentice")
                        .value(true))
                .andExpect(jsonPath("[" + numberOfProgrammers + "].deleted")
                        .value(false));

        mockMvc.perform(delete("/api/delete-programmers/" + lastProgrammer.getProgrammerId()))
                .andExpect(jsonPath("$.success")
                        .value(ProgrammingConstants.deleteSuccess("programmer")));
    }
}
