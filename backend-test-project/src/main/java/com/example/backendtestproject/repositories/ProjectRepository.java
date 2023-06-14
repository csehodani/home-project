package com.example.backendtestproject.repositories;

import com.example.backendtestproject.models.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {

    Project findByProjectId(Long id);

    void deleteByProjectId(Long id);
}
