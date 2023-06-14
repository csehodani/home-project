package com.example.backendtestproject.services;

import com.example.backendtestproject.constants.ProgrammingConstants;
import com.example.backendtestproject.dtos.ProjectManagerDetailsDto;
import com.example.backendtestproject.dtos.ProjectManagerDto;
import com.example.backendtestproject.dtos.ValidatorResultDto;
import com.example.backendtestproject.enums.SortableField;
import com.example.backendtestproject.models.Address;
import com.example.backendtestproject.models.BirthDate;
import com.example.backendtestproject.models.ProjectManager;
import com.example.backendtestproject.repositories.ProjectManagerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProjectManagerServiceImpl implements ProjectManagerService {
    private final ProjectManagerRepository projectManagerRepository;
    private final ValidatorService validatorService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public ValidatorResultDto save(ProjectManagerDto projectManager) {
        ValidatorResultDto result = validatorService.isProjectManagerValid(projectManager);

        if (result.isValid()) {
            ProjectManager modelProjectManager = modelMapper.map(projectManager, ProjectManager.class);

            if (projectManager.getProjectManagerId() == null) {
                Address modelAddress = modelProjectManager.getAddress();
                modelAddress.setProjectManager(modelProjectManager);

                BirthDate modelBirthDate = modelProjectManager.getBirthDate();
                modelBirthDate.setProjectManager(modelProjectManager);
            }

            modelProjectManager.setProject(null);

            projectManagerRepository.save(modelProjectManager);
        }

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectManagerDto> findAll() {
        return ((List<ProjectManager>) projectManagerRepository.findAll()).stream()
                .map(r -> modelMapper.map(r, ProjectManagerDto.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectManagerDto> findAllSorted(String sortBy, String order) {

        if (sortBy == null) {
            return findAll();
        }

        if (sortBy.equalsIgnoreCase(SortableField.NAME.toString())) {
            return findAllSortedByName(order);
        }

        if (sortBy.equalsIgnoreCase(SortableField.EMAIL.toString())) {
            return findAllSortedByEmail(order);
        }

        if (sortBy.equalsIgnoreCase(SortableField.PHONENUMBER.toString())) {
            return findAllSortedByPhoneNumber(order);
        }

        if (sortBy.equalsIgnoreCase(SortableField.CITY.toString())) {
            return findAllSortedByCity(order);
        }

        return findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectManagerDetailsDto findById(Long id) {
        return modelMapper.map(projectManagerRepository.findByProjectManagerId(id), ProjectManagerDetailsDto.class);
    }

    @Transactional
    @Override
    public ValidatorResultDto editById(Long id, ProjectManagerDto editedProjectManager) {

        ProjectManager originalProjectManager = projectManagerRepository.findByProjectManagerId(id);

        if (originalProjectManager == null) {
            return new ValidatorResultDto(false, ProgrammingConstants.noProjectManagerFound);
        }

        editedProjectManager.setProjectManagerId(id);

        if (editedProjectManager.getName() != null) {
            originalProjectManager.setName(editedProjectManager.getName());
        }
        else {
            return new ValidatorResultDto(false, ProgrammingConstants.nameMissing);
        }

        if (editedProjectManager.getAddress().getZipCode() != null) {
            originalProjectManager.getAddress().setZipCode(editedProjectManager.getAddress().getZipCode());
        }
        else {
            return new ValidatorResultDto(false, ProgrammingConstants.zipCodeInvalid);
        }

        if (editedProjectManager.getAddress().getCity() != null) {
            originalProjectManager.getAddress().setCity(editedProjectManager.getAddress().getCity());
        }
        else {
            return new ValidatorResultDto(false, ProgrammingConstants.cityMissing);
        }

        if (editedProjectManager.getAddress().getStreet() != null) {
            originalProjectManager.getAddress().setStreet(editedProjectManager.getAddress().getStreet());
        }
        else {
            return new ValidatorResultDto(false, ProgrammingConstants.streetMissing);
        }

        if (editedProjectManager.getPhoneNumber() != null) {
            originalProjectManager.setPhoneNumber(editedProjectManager.getPhoneNumber());
        }
        else {
            return new ValidatorResultDto(false, ProgrammingConstants.phoneNumberMissing);
        }


        if (editedProjectManager.getBirthDate().getDay() != null) {
            originalProjectManager.getBirthDate().setDay(editedProjectManager.getBirthDate().getDay());
        }
        else {
            return new ValidatorResultDto(false, ProgrammingConstants.birthDayMissing);
        }

        if (editedProjectManager.getBirthDate().getMonth() != null) {
            originalProjectManager.getBirthDate().setMonth(editedProjectManager.getBirthDate().getMonth());
        }
        else {
            return new ValidatorResultDto(false, ProgrammingConstants.birthMonthMissing);
        }

        if (editedProjectManager.getBirthDate().getYear() != null) {
            originalProjectManager.getBirthDate().setYear(editedProjectManager.getBirthDate().getYear());
        }
        else {
            return new ValidatorResultDto(false, ProgrammingConstants.birthYearMissing);
        }

        if (Objects.equals(editedProjectManager.getEmail(), originalProjectManager.getEmail())) {
            return new ValidatorResultDto(false, ProgrammingConstants.emailExists);
        }

        if (editedProjectManager.getEmail() != null) {
            originalProjectManager.setEmail(editedProjectManager.getEmail());
        }
        else {
            return new ValidatorResultDto(false, ProgrammingConstants.emailMissing);
        }

        if(validatorService.isProjectManagerValid(editedProjectManager).isValid()){
            projectManagerRepository.save(originalProjectManager);
        }


        return new ValidatorResultDto(true, ProgrammingConstants.saveSuccess("project manager"));
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        ProjectManager projectManager = projectManagerRepository.findByProjectManagerId(id);
        if (projectManager == null) {
            return false;
        }

        projectManager.setAddress(null);
        projectManager.setBirthDate(null);
        projectManager.setProject(null);

        projectManagerRepository.deleteByProjectManagerId(id);

        return true;
    }

    private List<ProjectManagerDto> findAllSortedByName(String order) {

        if (order == null || !order.equalsIgnoreCase("desc")) {
            return findAll().stream()
                    .sorted(Comparator.comparing(ProjectManagerDto::getName))
                    .toList();
        }

        return findAll().stream()
                .sorted(Comparator.comparing(ProjectManagerDto::getName).reversed())
                .toList();
    }

    private List<ProjectManagerDto> findAllSortedByEmail(String order) {

        if (order == null || !order.equalsIgnoreCase("desc")) {
            return findAll().stream()
                    .sorted(Comparator.comparing(ProjectManagerDto::getEmail)
                            .thenComparing(ProjectManagerDto::getName))
                    .toList();
        }

        return findAll().stream()
                .sorted(Comparator.comparing(ProjectManagerDto::getEmail).reversed()
                        .thenComparing(ProjectManagerDto::getName))
                .toList();
    }

    private List<ProjectManagerDto> findAllSortedByPhoneNumber(String order) {

        if (order == null || !order.equalsIgnoreCase("desc")) {
            return findAll().stream()
                    .sorted(Comparator.comparing(ProjectManagerDto::getPhoneNumber)
                            .thenComparing(ProjectManagerDto::getName))
                    .toList();
        }

        return findAll().stream()
                .sorted(Comparator.comparing(ProjectManagerDto::getPhoneNumber).reversed()
                        .thenComparing(ProjectManagerDto::getName))
                .toList();
    }

    private List<ProjectManagerDto> findAllSortedByCity(String order) {

        if (order == null || !order.equalsIgnoreCase("desc")) {
            return findAll().stream()
                    .sorted(Comparator.comparing(projectManager -> ((ProjectManagerDto) projectManager).getAddress().getCity())
                            .thenComparing(projectManager -> ((ProjectManagerDto) projectManager).getName()))
                    .toList();
        }

        return findAll().stream()
                .sorted(Comparator.comparing(projectManager -> ((ProjectManagerDto) projectManager).getAddress().getCity()).reversed()
                        .thenComparing(projectManager -> ((ProjectManagerDto) projectManager).getName()))
                .toList();
    }
}
