package com.example.backendtestproject.dtos;

public record ValidatorResultDto(boolean isValid, String message) {
    public ValidatorResultDto(boolean isValid) {
        this(isValid, "");
    }
}
