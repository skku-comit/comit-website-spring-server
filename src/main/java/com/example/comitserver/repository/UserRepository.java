package com.example.comitserver.repository;

import com.example.comitserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);

    Boolean existsByName(String username);

    Boolean existsByPhoneNumber(String PhoneNumber);

    Boolean existsByStudentId(String studentId);

    Boolean existsByEmail(String email);

    Optional<User> findByName(String name);

}
