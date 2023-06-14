package com.example.backendtestproject.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProjectManagerDetailsDto {
    private Long projectManagerId;

    private String name;

    private AddressDto address;

    private BirthDateDto birthDate;

    private String phoneNumber;

    private String email;

    private ProjectDto project;

    private Boolean deleted = Boolean.FALSE;
}
