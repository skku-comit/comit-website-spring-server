package com.example.comitserver.repository;

import com.example.comitserver.entity.Progress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
    List<Progress> findByStudyId(Long studyId);
}
