package com.example.backendtestproject.services;

import com.example.backendtestproject.constants.ProgrammingConstants;
import com.example.backendtestproject.dtos.ProjectDto;
import com.example.backendtestproject.dtos.ValidatorResultDto;
import com.example.backendtestproject.enums.SortableField;
import com.example.backendtestproject.models.Programmer;
import com.example.backendtestproject.models.Project;
import com.example.backendtestproject.models.ProjectManager;
import com.example.backendtestproject.repositories.ProgrammerRepository;
import com.example.backendtestproject.repositories.ProjectManagerRepository;
import com.example.backendtestproject.repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ModelMapper modelMapper;
    private final ValidatorService validatorService;
    private final ProjectManagerRepository projectManagerRepository;
    private final ProgrammerRepository programmerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDto> findAll() {
        return ((List<Project>) projectRepository.findAll()).stream()
                .map(r -> modelMapper.map(r, ProjectDto.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDto> findAllSorted(String sortBy, String order) {

        if (sortBy == null) {
            return findAll();
        }

        if (sortBy.equalsIgnoreCase(SortableField.CLIENT.toString())) {
            return findAllSortedByClient(order);
        }

        if (sortBy.equalsIgnoreCase(SortableField.STARTDATE.toString())) {
            return findAllSortedByStartDate(order);
        }

        if (sortBy.equalsIgnoreCase(SortableField.DESCRIPTION.toString())) {
            return findAllSortedByDescription(order);
        }

        return findAll();
    }

    @Override
    @Transactional
    public ValidatorResultDto save(ProjectDto project) {
        ValidatorResultDto result = validatorService.isProjectValid(project);

        if (result.isValid()) {
            Project modelProject = modelMapper.map(project, Project.class);

            projectRepository.save(modelProject);
        }

        return result;
    }

    @Override
    @Transactional
    public ValidatorResultDto saveByProjectManagerId(ProjectDto project, Long projectManagerId) {
        ValidatorResultDto result = validatorService.isProjectValid(project);

        if (result.isValid()) {

            ProjectManager modelProjectManager = projectManagerRepository
                    .findById(projectManagerId).orElseThrow(() -> new IllegalArgumentException(ProgrammingConstants.noProjectManagerFound));

            Project modelProject = modelMapper.map(project, Project.class);

            modelProjectManager.setProject(modelProject);

            projectManagerRepository.save(modelProjectManager);
        }
        return result;

    }

    @Override
    @Transactional
    public ValidatorResultDto saveByProgrammerId(ProjectDto project, Long programmerId) {
        ValidatorResultDto result = validatorService.isProjectValid(project);

        if (result.isValid()) {

            Programmer modelProgrammer = programmerRepository
                    .findById(programmerId).orElseThrow(() -> new IllegalArgumentException(ProgrammingConstants.noProgrammerFound));

            Project modelProject = modelMapper.map(project, Project.class);

            modelProgrammer.setProject(modelProject);

            programmerRepository.save(modelProgrammer);
        }
        return result;

    }

    @Override
    @Transactional(readOnly = true)
    public ProjectDto findById(Long id) {
        return modelMapper.map(projectRepository.findByProjectId(id), ProjectDto.class);
    }

    @Override
    @Transactional
    public ValidatorResultDto editById(Long id, ProjectDto editedProject) {

        Project originalProject = projectRepository.findByProjectId(id);

        if (originalProject == null) {
            return new ValidatorResultDto(false, ProgrammingConstants.noProjectFound);
        }

        editedProject.setProjectId(id);

        if (editedProject.getClient() != null) {
            originalProject.setClient(editedProject.getClient());
        } else {
            return new ValidatorResultDto(false, ProgrammingConstants.clientMissing);
        }

        if (editedProject.getDescription() != null) {
            originalProject.setDescription(editedProject.getDescription());
        } else {
            return new ValidatorResultDto(false, ProgrammingConstants.descriptionMissing);
        }

        if (editedProject.getStartDate() != null) {
            originalProject.setStartDate(editedProject.getStartDate());
        } else {
            return new ValidatorResultDto(false, ProgrammingConstants.startDateMissing);
        }

        projectRepository.save(originalProject);

        return new ValidatorResultDto(true, ProgrammingConstants.saveSuccess("project"));
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        Project project = projectRepository.findByProjectId(id);
        if (project == null) {
            return false;
        }

        projectRepository.deleteByProjectId(id);

        return true;
    }

    private List<ProjectDto> findAllSortedByClient(String order) {

        if (order == null || !order.equalsIgnoreCase("desc")) {
            return findAll().stream()
                    .sorted(Comparator.comparing(ProjectDto::getClient))
                    .toList();
        }

        return findAll().stream()
                .sorted(Comparator.comparing(ProjectDto::getClient).reversed())
                .toList();
    }

    private List<ProjectDto> findAllSortedByStartDate(String order) {

        if (order == null || !order.equalsIgnoreCase("desc")) {
            return findAll().stream()
                    .sorted(Comparator.comparing(ProjectDto::getStartDate)
                            .thenComparing(ProjectDto::getClient))
                    .toList();
        }

        return findAll().stream()
                .sorted(Comparator.comparing(ProjectDto::getStartDate).reversed()
                        .thenComparing(ProjectDto::getClient))
                .toList();
    }

    private List<ProjectDto> findAllSortedByDescription(String order) {

        if (order == null || !order.equalsIgnoreCase("desc")) {
            return findAll().stream()
                    .sorted(Comparator.comparing(ProjectDto::getDescription)
                            .thenComparing(ProjectDto::getClient))
                    .toList();
        }

        return findAll().stream()
                .sorted(Comparator.comparing(ProjectDto::getDescription).reversed()
                        .thenComparing(ProjectDto::getClient))
                .toList();
    }
}
