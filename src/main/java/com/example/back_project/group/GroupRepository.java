package com.example.back_project.group;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {

    @Override
    List<Group> findAll();
    Group save(Group group);
    void deleteById(Long id);
    Optional<Group> findById(Long id);

}
