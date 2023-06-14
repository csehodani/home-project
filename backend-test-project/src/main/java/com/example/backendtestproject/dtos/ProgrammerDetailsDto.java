package com.example.backendtestproject.dtos;

import com.example.backendtestproject.enums.Responsibility;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProgrammerDetailsDto {
    private Long programmerId;

    private String name;

    private AddressDto address;

    private BirthDateDto birthDate;

    private String phoneNumber;

    private String email;

    private Responsibility responsibility;

    private Boolean isApprentice;

    private ProjectDto project;

    private ProjectManagerDto projectManager;

    private Boolean deleted = Boolean.FALSE;
}
