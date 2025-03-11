package com.example.comitserver.repository;

import com.example.comitserver.entity.StudyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyUserRepository extends JpaRepository<StudyUser, Long> {
    List<StudyUser> findByStudyId(Long studyId);

    void deleteByStudyId(Long studyId);

    void deleteByUserId(Long userId);
}
