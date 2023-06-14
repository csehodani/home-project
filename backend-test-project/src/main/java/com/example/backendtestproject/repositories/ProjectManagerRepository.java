package com.example.backendtestproject.repositories;

import com.example.backendtestproject.models.ProjectManager;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectManagerRepository extends CrudRepository<ProjectManager, Long> {

    ProjectManager findByProjectManagerId(Long id);

    void deleteByProjectManagerId(Long id);

    Optional<ProjectManager> findByEmail(String email);
}
