package com.example.comitserver.repository;

import com.example.comitserver.entity.Role;
import com.example.comitserver.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findById(Long id);

    Optional<UserEntity> findByUsername(String username);

    List<UserEntity> findByIsStaff(Boolean isStaff);

    List<UserEntity> findByRole(Role role);

    List<UserEntity> findByRoleAndIsStaff(Role role, Boolean isStaff);

    Optional<UserEntity> findByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByPhoneNumber(String phoneNumber);

    Boolean existsByStudentId(String studentId);
}
