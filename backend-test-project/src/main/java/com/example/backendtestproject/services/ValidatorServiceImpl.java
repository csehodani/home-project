package com.example.backendtestproject.services;

import com.example.backendtestproject.constants.ProgrammingConstants;
import com.example.backendtestproject.dtos.*;
import com.example.backendtestproject.repositories.ProgrammerRepository;
import com.example.backendtestproject.repositories.ProjectManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
@RequiredArgsConstructor
public class ValidatorServiceImpl implements ValidatorService {

    private final ProjectManagerRepository projectManagerRepository;
    private final ProgrammerRepository programmerRepository;

    @Override
    public ValidatorResultDto isAddressValid(AddressDto address) {
        boolean isValid = address != null
                && address.getZipCode() != null
                && address.getZipCode() >= 1000
                && address.getZipCode() <= 9999
                && address.getCity() != null
                && !address.getCity().isBlank()
                && address.getStreet() != null
                && !address.getStreet().isBlank();

        StringBuilder message = new StringBuilder();

        if (isValid) {
            return new ValidatorResultDto(isValid, message.toString());
        }

        if (address == null) {
            message.append(ProgrammingConstants.zipCodeInvalid)
                    .append(ProgrammingConstants.cityMissing)
                    .append(ProgrammingConstants.streetMissing);
        } else {
            if (address.getZipCode() == null || !(address.getZipCode() >= 1000 && address.getZipCode() <= 9999)) {
                message.append(ProgrammingConstants.zipCodeInvalid);
            }
            if (address.getCity() == null || address.getCity().isBlank()) {
                message.append(ProgrammingConstants.cityMissing);
            }
            if (address.getStreet() == null || address.getStreet().isBlank()) {
                message.append(ProgrammingConstants.streetMissing);
            }
        }

        return new ValidatorResultDto(isValid, message.toString());
    }

    @Override
    public boolean isStartDateValidFormat(ProjectDto project) {
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            if (project.getStartDate() != null) {
                sdf.parse(project.getStartDate());
            }

        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    @Override
    public ValidatorResultDto isStartDateValid(ProjectDto project) {
        boolean isValid = isStartDateValidFormat(project);

        StringBuilder message = new StringBuilder();

        if (isValid) {
            return new ValidatorResultDto(isValid, message.toString());
        } else {
            message.append(ProgrammingConstants.startDateInvalid);
        }

        if (project.getStartDate() == null || project.getStartDate().isBlank()) {
            message.append(ProgrammingConstants.startDateMissing);
        }

        return new ValidatorResultDto(isValid, message.toString());
    }

    @Override
    public ValidatorResultDto isProjectManagerValid(ProjectManagerDto projectManager) {
        boolean isValid = projectManager != null
                && projectManager.getName() != null
                && !projectManager.getName().isBlank()
                && isAddressValid(projectManager.getAddress()).isValid()
                && isBirthDateValid(projectManager.getBirthDate()).isValid()
                && isProjectManagerPhoneNumberValid(projectManager).isValid()
                && isProjectManagerEmailValid(projectManager).isValid()
                && !isProjectManagerEmailDuplicated(projectManager);

        StringBuilder message = new StringBuilder();

        if (isValid) {
            message.append(ProgrammingConstants.saveSuccess("project manager"));
        } else {
            message.append(ProgrammingConstants.saveFail("project manager"));
            if (projectManager == null) {
                message.append(ProgrammingConstants.nameMissing)
                        .append(ProgrammingConstants.zipCodeInvalid)
                        .append(ProgrammingConstants.streetMissing)
                        .append(ProgrammingConstants.cityMissing)
                        .append(ProgrammingConstants.birthDayMissing)
                        .append(ProgrammingConstants.birthMonthMissing)
                        .append(ProgrammingConstants.birthYearMissing)
                        .append(ProgrammingConstants.phoneNumberMissing)
                        .append(ProgrammingConstants.emailMissing);
            } else {
                if (projectManager.getName() == null || projectManager.getName().isBlank()) {
                    message.append(ProgrammingConstants.nameMissing);
                }
                if (isProjectManagerEmailDuplicated(projectManager)) {
                    message.append(ProgrammingConstants.emailExists);
                }

                message.append(isAddressValid(projectManager.getAddress()).message());

                message.append(isBirthDateValid(projectManager.getBirthDate()).message());

                message.append(isProjectManagerPhoneNumberValid(projectManager).message());

                message.append(isProjectManagerEmailValid(projectManager).message());
            }
        }

        return new ValidatorResultDto(isValid, message.toString());
    }

    @Override
    public ValidatorResultDto isProgrammerValid(ProgrammerDto programmer) {
        boolean isValid = programmer != null
                && programmer.getName() != null
                && !programmer.getName().isBlank()
                && isAddressValid(programmer.getAddress()).isValid()
                && isBirthDateValid(programmer.getBirthDate()).isValid()
                && isProgrammerPhoneNumberValid(programmer).isValid()
                && isProgrammerEmailValid(programmer).isValid()
                && !isProgrammerEmailDuplicated(programmer)
                && programmer.getResponsibility() != null
                && programmer.getIsApprentice() != null;

        StringBuilder message = new StringBuilder();

        if (isValid) {
            message.append(ProgrammingConstants.saveSuccess("programmer"));
        } else {
            message.append(ProgrammingConstants.saveFail("programmer"));
            if (programmer == null) {
                message.append(ProgrammingConstants.nameMissing)
                        .append(ProgrammingConstants.zipCodeInvalid)
                        .append(ProgrammingConstants.streetMissing)
                        .append(ProgrammingConstants.cityMissing)
                        .append(ProgrammingConstants.responsibilityMissing)
                        .append(ProgrammingConstants.apprenticeMissing)
                        .append(ProgrammingConstants.birthDayMissing)
                        .append(ProgrammingConstants.birthMonthMissing)
                        .append(ProgrammingConstants.birthYearMissing)
                        .append(ProgrammingConstants.phoneNumberMissing)
                        .append(ProgrammingConstants.emailMissing);
            } else {
                if (programmer.getName() == null || programmer.getName().isBlank()) {
                    message.append(ProgrammingConstants.nameMissing);
                }
                if (programmer.getResponsibility() == null) {
                    message.append(ProgrammingConstants.responsibilityMissing);
                }
                if (programmer.getIsApprentice() == null) {
                    message.append(ProgrammingConstants.apprenticeMissing);
                }
                if (isProgrammerEmailDuplicated(programmer)) {
                    message.append(ProgrammingConstants.emailExists);
                }

                message.append(isAddressValid(programmer.getAddress()).message());

                message.append(isBirthDateValid(programmer.getBirthDate()).message());

                message.append(isProgrammerPhoneNumberValid(programmer).message());

                message.append(isProgrammerEmailValid(programmer).message());
            }
        }

        return new ValidatorResultDto(isValid, message.toString());
    }

    @Override
    public ValidatorResultDto isProjectValid(ProjectDto project) {
        boolean isValid = project != null
                && project.getClient() != null
                && !project.getClient().isBlank()
                && project.getDescription() != null
                && !project.getDescription().isBlank()
                && project.getStartDate() != null
                && !project.getStartDate().isBlank()
                && isStartDateValid(project).isValid();

        StringBuilder message = new StringBuilder();

        if (isValid) {
            message.append(ProgrammingConstants.saveSuccess("project"));
        } else {
            message.append(ProgrammingConstants.saveFail("project"));
            if (project == null) {
                message.append(ProgrammingConstants.clientMissing)
                        .append(ProgrammingConstants.descriptionMissing)
                        .append(ProgrammingConstants.startDateMissing);
            } else {
                if (project.getClient() == null || project.getClient().isBlank()) {
                    message.append(ProgrammingConstants.clientMissing);
                }
                if (project.getDescription() == null || project.getDescription().isBlank()) {
                    message.append(ProgrammingConstants.descriptionMissing);
                }

                if (project.getStartDate() == null || project.getStartDate().isBlank()) {
                    message.append(ProgrammingConstants.startDateMissing);
                }

                message.append(isStartDateValid(project).message());
            }
        }

        return new ValidatorResultDto(isValid, message.toString());
    }

    @Override
    public ValidatorResultDto isBirthDateValid(BirthDateDto birthDate) {
        boolean isValid = birthDate != null
                && birthDate.getDay() != null
                && birthDate.getDay() >= 1
                && birthDate.getDay() <= 31
                && birthDate.getMonth() != null
                && birthDate.getMonth() >= 1
                && birthDate.getMonth() <= 12
                && birthDate.getYear() != null
                && birthDate.getYear() >= 1900
                && birthDate.getYear() <= 2023;


        StringBuilder message = new StringBuilder();

        if (isValid) {
            return new ValidatorResultDto(isValid, message.toString());
        }

        if (birthDate == null) {
            message.append(ProgrammingConstants.birthDayMissing)
                    .append(ProgrammingConstants.birthMonthMissing)
                    .append(ProgrammingConstants.birthYearMissing);
        } else {
            if (birthDate.getDay() == null || !(birthDate.getDay() >= 1 && birthDate.getDay() <= 31)) {
                message.append(ProgrammingConstants.birthDayInvalid);
            }
            if (birthDate.getMonth() == null || !(birthDate.getMonth() >= 1 && birthDate.getMonth() <= 12)) {
                message.append(ProgrammingConstants.birthMonthInvalid);
            }
            if (birthDate.getYear() == null || !(birthDate.getYear() >= 1900 && birthDate.getYear() <= 2023)) {
                message.append(ProgrammingConstants.birthYearInvalid);
            }
        }

        return new ValidatorResultDto(isValid, message.toString());
    }

    @Override
    public ValidatorResultDto isProgrammerPhoneNumberValid(ProgrammerDto programmer) {
        boolean isValid = programmer.getPhoneNumber() != null
                && !programmer.getPhoneNumber().isBlank()
                && programmer.getPhoneNumber().length() == 12
                && (programmer.getPhoneNumber().startsWith("+3630")
                || programmer.getPhoneNumber().startsWith("+3620")
                || programmer.getPhoneNumber().startsWith("+3650")
                || programmer.getPhoneNumber().startsWith("+3670"));


        StringBuilder message = new StringBuilder();

        if (isValid) {
            return new ValidatorResultDto(isValid, message.toString());
        }

        if (programmer.getPhoneNumber() == null) {
            message.append(ProgrammingConstants.phoneNumberMissing);
        } else {
            if (programmer.getPhoneNumber().length() != 12
                    && !programmer.getPhoneNumber().startsWith("+3630")
                    || !programmer.getPhoneNumber().startsWith("+3620")
                    || !programmer.getPhoneNumber().startsWith("+3650")
                    || !programmer.getPhoneNumber().startsWith("+3670")) {
                message.append(ProgrammingConstants.phoneNumberInvalid);
            }
        }

        return new ValidatorResultDto(isValid, message.toString());
    }

    @Override
    public ValidatorResultDto isProjectManagerPhoneNumberValid(ProjectManagerDto projectManager) {
        boolean isValid = projectManager.getPhoneNumber() != null
                && !projectManager.getPhoneNumber().isBlank()
                && projectManager.getPhoneNumber().length() == 12
                && (projectManager.getPhoneNumber().startsWith("+3630")
                || projectManager.getPhoneNumber().startsWith("+3620")
                || projectManager.getPhoneNumber().startsWith("+3650")
                || projectManager.getPhoneNumber().startsWith("+3670"));


        StringBuilder message = new StringBuilder();

        if (isValid) {
            return new ValidatorResultDto(isValid, message.toString());
        }

        if (projectManager.getPhoneNumber() == null) {
            message.append(ProgrammingConstants.phoneNumberMissing);
        } else {
            if (projectManager.getPhoneNumber().length() != 12
                    && !projectManager.getPhoneNumber().startsWith("+3630")
                    || !projectManager.getPhoneNumber().startsWith("+3620")
                    || !projectManager.getPhoneNumber().startsWith("+3650")
                    || !projectManager.getPhoneNumber().startsWith("+3670")) {
                message.append(ProgrammingConstants.phoneNumberInvalid);
            }
        }

        return new ValidatorResultDto(isValid, message.toString());
    }

    @Override
    public ValidatorResultDto isProjectManagerEmailValid(ProjectManagerDto projectManager) {
        boolean isValid = projectManager.getEmail() != null
                && !projectManager.getEmail().isBlank()
                && projectManager.getEmail().endsWith(".com")
                && projectManager.getEmail().contains("@");


        StringBuilder message = new StringBuilder();

        if (isValid) {
            return new ValidatorResultDto(isValid, message.toString());
        }

        if (projectManager.getEmail() == null) {
            message.append(ProgrammingConstants.emailMissing);
        } else {
            if (!projectManager.getEmail().endsWith(".com")) {
                message.append(ProgrammingConstants.comIsMissing);
            }
            if (!projectManager.getEmail().contains("@")) {
                message.append(ProgrammingConstants.atIsMissing);
            }
        }

        return new ValidatorResultDto(isValid, message.toString());
    }

    @Override
    public ValidatorResultDto isProgrammerEmailValid(ProgrammerDto programmer) {
        boolean isValid = programmer.getEmail() != null
                && !programmer.getEmail().isBlank()
                && programmer.getEmail().endsWith(".com")
                && programmer.getEmail().contains("@");


        StringBuilder message = new StringBuilder();

        if (isValid) {
            return new ValidatorResultDto(isValid, message.toString());
        }

        if (programmer.getEmail() == null) {
            message.append(ProgrammingConstants.emailMissing);
        } else {
            if (!programmer.getEmail().endsWith(".com")) {
                message.append(ProgrammingConstants.comIsMissing);
            }
            if (!programmer.getEmail().contains("@")) {
                message.append(ProgrammingConstants.atIsMissing);
            }
        }

        return new ValidatorResultDto(isValid, message.toString());
    }

    private boolean isProjectManagerEmailDuplicated(ProjectManagerDto projectManager) {
        return projectManagerRepository.findByEmail(projectManager.getEmail()).isPresent();
    }

    private boolean isProgrammerEmailDuplicated(ProgrammerDto programmer) {
        return programmerRepository.findByEmail(programmer.getEmail()).isPresent();
    }
}
