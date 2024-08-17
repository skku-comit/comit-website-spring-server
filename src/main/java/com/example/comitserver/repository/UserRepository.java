package com.example.comitserver.repository;

import com.example.comitserver.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findById(Long id);

    Optional<UserEntity> findByUsername(String username);
    // Find all users by staff status
    List<UserEntity> findAllByIsStaff(Boolean isStaff);

    // Find user by email (for other operations if needed)
    Optional<UserEntity> findByEmail(String email);

    // Check if email exists
    Boolean existsByEmail(String email);

    // Check if phone number exists
    Boolean existsByPhoneNumber(String phoneNumber);

    // Check if student ID exists
    Boolean existsByStudentId(String studentId);
}
