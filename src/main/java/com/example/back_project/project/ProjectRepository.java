package com.example.back_project.project;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Override
    Project save(Project project);

    @Override
    List<Project> findAll();

    @Override
    void deleteById(Long id);

    @Override
    Optional<Project> findById(Long id);
}