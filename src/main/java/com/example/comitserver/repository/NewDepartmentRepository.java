package com.example.comitserver.repository;

import com.example.comitserver.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NewDepartmentRepository extends JpaRepository<Team, Long> {

    Optional<Team> findByDepartmentId(Long departmentId);

    Optional<Team> findByDepartmentName(String departmentName);
}
