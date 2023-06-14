package com.example.backendtestproject.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddressDto {

    private Long address_id;

    private Integer zipCode;

    private String city;

    private String street;
}
