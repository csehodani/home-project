package com.example.backendtestproject.controllers;

import com.example.backendtestproject.constants.ProgrammingConstants;
import com.example.backendtestproject.dtos.*;
import com.example.backendtestproject.services.ProjectManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProjectManagerRestController {

    private final ProjectManagerService projectManagerService;

    @GetMapping("/api/project-managers")
    public ResponseEntity<?> apiListProjectManagers(@RequestParam(value = "sortby", required = false) String sortBy,
                                                    @RequestParam(value = "order", required = false) String order) {
        List<ProjectManagerDto> projectManagers = projectManagerService.findAllSorted(sortBy, order);

        if (projectManagers.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorDto(ProgrammingConstants.noProjectManagerFound));
        }
        return ResponseEntity.ok(projectManagers);
    }

    @PostMapping("/api/add-project-managers")
    public ResponseEntity<?> apiAddProjectManagers(@RequestBody ProjectManagerDto projectManager) {

        ValidatorResultDto result = projectManagerService.save(projectManager);

        if (!result.isValid()) {
            return ResponseEntity.badRequest().body(new ErrorDto(result.message()));
        }
        return ResponseEntity.ok(new SuccessDto(result.message()));
    }

    @PostMapping("/api/edit-project-managers/{id}")
    public ResponseEntity<?> apiEditProjectManager(@PathVariable Long id,
                                                   @RequestBody ProjectManagerDto projectManager) {

        ValidatorResultDto result = projectManagerService.editById(id, projectManager);

        if (!result.isValid()) {
            return ResponseEntity.badRequest().body(new ErrorDto(result.message()));
        }
        return ResponseEntity.ok(new SuccessDto(result.message()));
    }

    @GetMapping("/api/details-project-managers/{id}")
    public ResponseEntity<?> apiDetailsProjectManager(@PathVariable Long id) {

        ProjectManagerDetailsDto projectManager = projectManagerService.findById(id);

        if (projectManager == null) {
            return ResponseEntity.badRequest().body(new ErrorDto(ProgrammingConstants.noProjectManagerFound));
        }
        return ResponseEntity.ok(projectManager);
    }

    @DeleteMapping("/api/delete-project-managers/{id}")
    public ResponseEntity<?> apiDeleteProjectManager(@PathVariable Long id) {

        if (!projectManagerService.deleteById(id)) {
            return ResponseEntity.badRequest().body(new ErrorDto(ProgrammingConstants.noProjectManagerFound));
        }
        return ResponseEntity.ok(new SuccessDto(ProgrammingConstants.deleteSuccess("project manager")));
    }
}
