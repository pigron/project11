package com.example.back_project.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Override
    List<User> findAll(); //모든 데이터 검색

    @Override
    User save(User user); //JpaRepository.save() : insert, update 가능

    @Override
    Optional<User> findById(Long id); //findById() : Id에 매칭되는 데이터 검색

//    Optional<User> findByUserId(String userId); //로그인 검증
//
    boolean existsByUsername(String userName);

    boolean existsByPassword(String userPassword);

    @Override
    void deleteById(Long id); //deleteById() : Id에 매칭되는 데이터 삭제

    Optional<User> findByUsername(String name);

}