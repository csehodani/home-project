package com.example.backendtestproject.controllers;

import com.example.backendtestproject.constants.ProgrammingConstants;
import com.example.backendtestproject.dtos.ErrorDto;
import com.example.backendtestproject.dtos.ProjectDto;
import com.example.backendtestproject.dtos.SuccessDto;
import com.example.backendtestproject.dtos.ValidatorResultDto;
import com.example.backendtestproject.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProjectRestController {

    private final ProjectService projectService;

    @GetMapping("/api/projects")
    public ResponseEntity<?> apiListProjects(@RequestParam(value = "sortby", required = false) String sortBy,
                                             @RequestParam(value = "order", required = false) String order) {
        List<ProjectDto> projects = projectService.findAllSorted(sortBy, order);

        if (projects.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorDto(ProgrammingConstants.noProjectFound));
        }
        return ResponseEntity.ok(projects);
    }

    @PostMapping("/api/add-projects")
    public ResponseEntity<?> apiAddProjects(@RequestBody ProjectDto project) {

        ValidatorResultDto result = projectService.save(project);

        if (!result.isValid()) {
            return ResponseEntity.badRequest().body(new ErrorDto(result.message()));
        }
        return ResponseEntity.ok(new SuccessDto(result.message()));
    }

    @PostMapping("/api/project-managers/{projectManagerId}/add-project")
    public ResponseEntity<?> apiAddProjectByProjectManagerId(@PathVariable Long projectManagerId,
                                                             @RequestBody ProjectDto project) {

        ValidatorResultDto result = projectService.saveByProjectManagerId(project, projectManagerId);

        if (!result.isValid()) {
            return ResponseEntity.badRequest().body(new ErrorDto(result.message()));
        }
        return ResponseEntity.ok(new SuccessDto(result.message()));
    }

    @PostMapping("/api/programmers/{programmerId}/add-project")
    public ResponseEntity<?> apiAddProjectByProgrammerId(@PathVariable Long programmerId,
                                                         @RequestBody ProjectDto project) {

        ValidatorResultDto result = projectService.saveByProgrammerId(project, programmerId);

        if (!result.isValid()) {
            return ResponseEntity.badRequest().body(new ErrorDto(result.message()));
        }
        return ResponseEntity.ok(new SuccessDto(result.message()));
    }

    @PostMapping("/api/edit-projects/{id}")
    public ResponseEntity<?> apiEditProject(@PathVariable Long id,
                                            @RequestBody ProjectDto project) {

        ValidatorResultDto result = projectService.editById(id, project);

        if (!result.isValid()) {
            return ResponseEntity.badRequest().body(new ErrorDto(result.message()));
        }
        return ResponseEntity.ok(new SuccessDto(result.message()));
    }

    @GetMapping("/api/details-projects/{id}")
    public ResponseEntity<?> apiDetailsProject(@PathVariable Long id) {

        ProjectDto project = projectService.findById(id);

        if (project == null) {
            return ResponseEntity.badRequest().body(new ErrorDto(ProgrammingConstants.noProjectFound));
        }
        return ResponseEntity.ok(project);
    }

    @DeleteMapping("/api/delete-projects/{id}")
    public ResponseEntity<?> apiDeleteProject(@PathVariable Long id) {

        if (!projectService.deleteById(id)) {
            return ResponseEntity.badRequest().body(new ErrorDto(ProgrammingConstants.noProjectFound));
        }
        return ResponseEntity.ok(new SuccessDto(ProgrammingConstants.deleteSuccess("project")));
    }
}
