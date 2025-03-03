package com.example.comitserver.repository;

import com.example.comitserver.entity.TeamUser;
import com.example.comitserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NewUserRepository  extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);

    Boolean existsByUsername(String username);

    Boolean existsByPhoneNumber(String PhoneNumber);

    Boolean existsByStudentId(String studentId);

    Boolean existsByEmail(String email);

}
