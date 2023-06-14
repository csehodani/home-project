package com.example.backendtestproject.services;

import com.example.backendtestproject.dtos.*;

public interface ValidatorService {

    ValidatorResultDto isProjectManagerValid(ProjectManagerDto projectManager);

    ValidatorResultDto isProgrammerValid(ProgrammerDto programmer);

    ValidatorResultDto isProjectValid(ProjectDto project);

    ValidatorResultDto isAddressValid(AddressDto address);

    ValidatorResultDto isBirthDateValid(BirthDateDto birthDate);

    ValidatorResultDto isProgrammerPhoneNumberValid(ProgrammerDto programmer);

    ValidatorResultDto isProjectManagerPhoneNumberValid(ProjectManagerDto projectManager);

    ValidatorResultDto isProjectManagerEmailValid(ProjectManagerDto projectManager);

    ValidatorResultDto isProgrammerEmailValid(ProgrammerDto programmer);

    boolean isStartDateValidFormat(ProjectDto project);

    ValidatorResultDto isStartDateValid(ProjectDto project);
}
