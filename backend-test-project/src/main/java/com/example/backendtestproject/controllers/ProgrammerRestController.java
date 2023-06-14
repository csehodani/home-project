package com.example.backendtestproject.controllers;

import com.example.backendtestproject.constants.ProgrammingConstants;
import com.example.backendtestproject.dtos.*;
import com.example.backendtestproject.services.ProgrammerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProgrammerRestController {

    private final ProgrammerService programmerService;

    @GetMapping("/api/programmers")
    public ResponseEntity<?> apiListProgrammers(@RequestParam(value = "sortby", required = false) String sortBy,
                                                @RequestParam(value = "order", required = false) String order) {
        List<ProgrammerDto> programmers = programmerService.findAllSorted(sortBy, order);

        if (programmers.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorDto(ProgrammingConstants.noProgrammerFound));
        }
        return ResponseEntity.ok(programmers);
    }

    @PostMapping("/api/add-programmers")
    public ResponseEntity<?> apiAddProgrammers(@RequestBody ProgrammerDto programmer) {

        ValidatorResultDto result = programmerService.save(programmer);

        if (!result.isValid()) {
            return ResponseEntity.badRequest().body(new ErrorDto(result.message()));
        }
        return ResponseEntity.ok(new SuccessDto(result.message()));
    }

    @PostMapping("/api/edit-programmers/{id}")
    public ResponseEntity<?> apiEditProgrammer(@PathVariable Long id,
                                               @RequestBody ProgrammerDto programmer) {

        ValidatorResultDto result = programmerService.editById(id, programmer);

        if (!result.isValid()) {
            return ResponseEntity.badRequest().body(new ErrorDto(result.message()));
        }
        return ResponseEntity.ok(new SuccessDto(result.message()));
    }

    @GetMapping("/api/details-programmers/{id}")
    public ResponseEntity<?> apiDetailsProgrammer(@PathVariable Long id) {

        ProgrammerDetailsDto programmer = programmerService.findById(id);

        if (programmer == null) {
            return ResponseEntity.badRequest().body(new ErrorDto(ProgrammingConstants.noProgrammerFound));
        }
        return ResponseEntity.ok(programmer);
    }

    @PostMapping("/api/project-managers/{projectManagerId}/add-programmers")
    public ResponseEntity<?> apiAddProgrammersByProjectManagerId(@PathVariable Long projectManagerId,
                                                                 @RequestBody ProgrammerDto programmer) {

        ValidatorResultDto result = programmerService.saveByProjectManagerId(programmer, projectManagerId);

        if (!result.isValid()) {
            return ResponseEntity.badRequest().body(new ErrorDto(result.message()));
        }
        return ResponseEntity.ok(new SuccessDto(result.message()));
    }

    @DeleteMapping("/api/delete-programmers/{id}")
    public ResponseEntity<?> apiDeleteProgrammer(@PathVariable Long id) {

        if (!programmerService.deleteById(id)) {
            return ResponseEntity.badRequest().body(new ErrorDto(ProgrammingConstants.noProgrammerFound));
        }
        return ResponseEntity.ok(new SuccessDto(ProgrammingConstants.deleteSuccess("programmer")));
    }
}
