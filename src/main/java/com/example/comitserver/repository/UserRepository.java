package com.example.comitserver.repository;

import com.example.comitserver.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByName(String name);
    Optional<User> findByEmail(String email);
    Optional<User> findByStudentId(String studentId);
    List<User> findByIsStaff(boolean isStaff); // 'isStaff' 필드 기준으로 쿼리 생성

}