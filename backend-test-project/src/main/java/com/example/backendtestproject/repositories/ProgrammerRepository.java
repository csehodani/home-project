package com.example.backendtestproject.repositories;

import com.example.backendtestproject.models.Programmer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProgrammerRepository extends CrudRepository<Programmer, Long> {

    Programmer findByProgrammerId(Long id);

    Optional<Programmer> findByEmail(String email);

    void deleteByProgrammerId(Long id);
}
