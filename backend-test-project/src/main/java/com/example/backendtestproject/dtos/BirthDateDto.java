package com.example.backendtestproject.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BirthDateDto {

    private Long birth_date_id;

    private Integer day;

    private Integer month;

    private Integer year;
}
