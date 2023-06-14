package com.example.backendtestproject.services;

import com.example.backendtestproject.constants.ProgrammingConstants;
import com.example.backendtestproject.dtos.ProgrammerDetailsDto;
import com.example.backendtestproject.dtos.ProgrammerDto;
import com.example.backendtestproject.dtos.ValidatorResultDto;
import com.example.backendtestproject.enums.SortableField;
import com.example.backendtestproject.models.Address;
import com.example.backendtestproject.models.BirthDate;
import com.example.backendtestproject.models.Programmer;
import com.example.backendtestproject.models.ProjectManager;
import com.example.backendtestproject.repositories.ProgrammerRepository;
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
public class ProgrammerServiceImpl implements ProgrammerService {
    private final ProgrammerRepository programmerRepository;
    private final ProjectManagerRepository projectManagerRepository;
    private final ValidatorService validatorService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public ValidatorResultDto save(ProgrammerDto programmer) {
        ValidatorResultDto result = validatorService.isProgrammerValid(programmer);

        if (result.isValid()) {
            Programmer modelProgrammer = modelMapper.map(programmer, Programmer.class);

            if (programmer.getProgrammerId() == null) {
                Address modelAddress = modelProgrammer.getAddress();
                modelAddress.setProgrammer(modelProgrammer);

                BirthDate modelBirthDate = modelProgrammer.getBirthDate();
                modelBirthDate.setProgrammer(modelProgrammer);
            }

            programmerRepository.save(modelProgrammer);
        }

        return result;
    }

    @Override
    @Transactional
    public ValidatorResultDto saveByProjectManagerId(ProgrammerDto programmer, Long projectManagerId) {
        ValidatorResultDto result = validatorService.isProgrammerValid(programmer);

        if (result.isValid()) {

            ProjectManager modelProjectManager = projectManagerRepository
                    .findById(projectManagerId).orElseThrow(() -> new IllegalArgumentException(ProgrammingConstants.noProjectManagerFound));

            Programmer modelProgrammer = modelMapper.map(programmer, Programmer.class);
            if (programmer.getProgrammerId() == null) {
                Address modelAddress = modelProgrammer.getAddress();
                modelAddress.setProgrammer(modelProgrammer);

                BirthDate modelBirthDate = modelProgrammer.getBirthDate();
                modelBirthDate.setProgrammer(modelProgrammer);

            } else {
                modelProgrammer = programmerRepository.findByProgrammerId(programmer.getProgrammerId());
            }
            modelProjectManager.addProgrammer(modelProgrammer);

            projectManagerRepository.save(modelProjectManager);
        }
        return result;

    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgrammerDto> findAll() {
        return ((List<Programmer>) programmerRepository.findAll()).stream()
                .map(r -> modelMapper.map(r, ProgrammerDto.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgrammerDto> findAllSorted(String sortBy, String order) {

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

        if (sortBy.equalsIgnoreCase(SortableField.ISAPPRENTICE.toString())) {
            return findAllSortedByApprentice(order);
        }

        if (sortBy.equalsIgnoreCase(SortableField.RESPONSIBILITY.toString())) {
            return findAllSortedByResponsibility(order);
        }

        return findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public ProgrammerDetailsDto findById(Long id) {
        return modelMapper.map(programmerRepository.findByProgrammerId(id), ProgrammerDetailsDto.class);
    }

    @Transactional
    @Override
    public ValidatorResultDto editById(Long id, ProgrammerDto editedProgrammer) {

        Programmer originalProgrammer = programmerRepository.findByProgrammerId(id);

        if (originalProgrammer == null) {
            return new ValidatorResultDto(false, ProgrammingConstants.noProgrammerFound);
        }

        editedProgrammer.setProgrammerId(id);

        if (editedProgrammer.getName() != null) {
            originalProgrammer.setName(editedProgrammer.getName());
        } else {
            return new ValidatorResultDto(false, ProgrammingConstants.nameMissing);
        }

        if (editedProgrammer.getAddress().getZipCode() != null) {
            originalProgrammer.getAddress().setZipCode(editedProgrammer.getAddress().getZipCode());
        } else {
            return new ValidatorResultDto(false, ProgrammingConstants.zipCodeInvalid);
        }

        if (editedProgrammer.getAddress().getCity() != null) {
            originalProgrammer.getAddress().setCity(editedProgrammer.getAddress().getCity());
        } else {
            return new ValidatorResultDto(false, ProgrammingConstants.cityMissing);
        }

        if (editedProgrammer.getAddress().getStreet() != null) {
            originalProgrammer.getAddress().setStreet(editedProgrammer.getAddress().getStreet());
        } else {
            return new ValidatorResultDto(false, ProgrammingConstants.streetMissing);
        }

        if (editedProgrammer.getPhoneNumber() != null) {
            originalProgrammer.setPhoneNumber(editedProgrammer.getPhoneNumber());
        } else {
            return new ValidatorResultDto(false, ProgrammingConstants.phoneNumberMissing);
        }


        if (editedProgrammer.getBirthDate().getDay() != null) {
            originalProgrammer.getBirthDate().setDay(editedProgrammer.getBirthDate().getDay());
        } else {
            return new ValidatorResultDto(false, ProgrammingConstants.birthDayMissing);
        }

        if (editedProgrammer.getBirthDate().getMonth() != null) {
            originalProgrammer.getBirthDate().setMonth(editedProgrammer.getBirthDate().getMonth());
        } else {
            return new ValidatorResultDto(false, ProgrammingConstants.birthMonthMissing);
        }

        if (editedProgrammer.getBirthDate().getYear() != null) {
            originalProgrammer.getBirthDate().setYear(editedProgrammer.getBirthDate().getYear());
        } else {
            return new ValidatorResultDto(false, ProgrammingConstants.birthYearMissing);
        }

        if (editedProgrammer.getIsApprentice() != null) {
            originalProgrammer.setIsApprentice(editedProgrammer.getIsApprentice());
        } else {
            return new ValidatorResultDto(false, ProgrammingConstants.apprenticeMissing);
        }

        if (editedProgrammer.getResponsibility() != null) {
            originalProgrammer.setResponsibility(editedProgrammer.getResponsibility());
        } else {
            return new ValidatorResultDto(false, ProgrammingConstants.responsibilityMissing);
        }

        if (Objects.equals(editedProgrammer.getEmail(), originalProgrammer.getEmail())) {
            return new ValidatorResultDto(false, ProgrammingConstants.emailExists);
        }

        if (editedProgrammer.getEmail() != null) {
            originalProgrammer.setEmail(editedProgrammer.getEmail());
        } else {
            return new ValidatorResultDto(false, ProgrammingConstants.emailMissing);
        }

        if (validatorService.isProgrammerValid(editedProgrammer).isValid()) {
            programmerRepository.save(originalProgrammer);
        }


        return new ValidatorResultDto(true, ProgrammingConstants.saveSuccess("programmer"));
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        Programmer programmer = programmerRepository.findByProgrammerId(id);
        if (programmer == null) {
            return false;
        }

        programmer.setAddress(null);
        programmer.setBirthDate(null);
        programmer.setProject(null);

        programmerRepository.deleteByProgrammerId(id);

        return true;
    }

    private List<ProgrammerDto> findAllSortedByName(String order) {

        if (order == null || !order.equalsIgnoreCase("desc")) {
            return findAll().stream()
                    .sorted(Comparator.comparing(ProgrammerDto::getName))
                    .toList();
        }

        return findAll().stream()
                .sorted(Comparator.comparing(ProgrammerDto::getName).reversed())
                .toList();
    }

    private List<ProgrammerDto> findAllSortedByEmail(String order) {

        if (order == null || !order.equalsIgnoreCase("desc")) {
            return findAll().stream()
                    .sorted(Comparator.comparing(ProgrammerDto::getEmail)
                            .thenComparing(ProgrammerDto::getName))
                    .toList();
        }

        return findAll().stream()
                .sorted(Comparator.comparing(ProgrammerDto::getEmail).reversed()
                        .thenComparing(ProgrammerDto::getName))
                .toList();
    }

    private List<ProgrammerDto> findAllSortedByPhoneNumber(String order) {

        if (order == null || !order.equalsIgnoreCase("desc")) {
            return findAll().stream()
                    .sorted(Comparator.comparing(ProgrammerDto::getPhoneNumber)
                            .thenComparing(ProgrammerDto::getName))
                    .toList();
        }

        return findAll().stream()
                .sorted(Comparator.comparing(ProgrammerDto::getPhoneNumber).reversed()
                        .thenComparing(ProgrammerDto::getName))
                .toList();
    }

    private List<ProgrammerDto> findAllSortedByCity(String order) {

        if (order == null || !order.equalsIgnoreCase("desc")) {
            return findAll().stream()
                    .sorted(Comparator.comparing(programmer -> ((ProgrammerDto) programmer).getAddress().getCity())
                            .thenComparing(programmer -> ((ProgrammerDto) programmer).getName()))
                    .toList();
        }

        return findAll().stream()
                .sorted(Comparator.comparing(programmer -> ((ProgrammerDto) programmer).getAddress().getCity()).reversed()
                        .thenComparing(programmer -> ((ProgrammerDto) programmer).getName()))
                .toList();
    }

    private List<ProgrammerDto> findAllSortedByApprentice(String order) {

        if (order == null || !order.equalsIgnoreCase("desc")) {
            return findAll().stream()
                    .sorted(Comparator.comparing(ProgrammerDto::getIsApprentice)
                            .thenComparing(ProgrammerDto::getName))
                    .toList();
        }

        return findAll().stream()
                .sorted(Comparator.comparing(ProgrammerDto::getIsApprentice).reversed()
                        .thenComparing(ProgrammerDto::getName))
                .toList();
    }

    private List<ProgrammerDto> findAllSortedByResponsibility(String order) {

        if (order == null || !order.equalsIgnoreCase("desc")) {
            return findAll().stream()
                    .sorted(Comparator.comparing(ProgrammerDto::getResponsibility)
                            .thenComparing(ProgrammerDto::getName))
                    .toList();
        }

        return findAll().stream()
                .sorted(Comparator.comparing(ProgrammerDto::getResponsibility).reversed()
                        .thenComparing(ProgrammerDto::getName))
                .toList();
    }
}
