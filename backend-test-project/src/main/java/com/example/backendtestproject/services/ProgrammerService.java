package com.example.backendtestproject.services;

import com.example.backendtestproject.dtos.ProgrammerDetailsDto;
import com.example.backendtestproject.dtos.ProgrammerDto;
import com.example.backendtestproject.dtos.ValidatorResultDto;

import java.util.List;

public interface ProgrammerService {
    ValidatorResultDto save(ProgrammerDto programmer);

    ValidatorResultDto saveByProjectManagerId(ProgrammerDto programmer, Long projectManagerId);

    List<ProgrammerDto> findAll();

    List<ProgrammerDto> findAllSorted(String sortBy, String order);

    ProgrammerDetailsDto findById(Long id);

    ValidatorResultDto editById(Long id, ProgrammerDto programmer);

    boolean deleteById(Long id);
}
