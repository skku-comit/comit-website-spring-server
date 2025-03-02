package com.example.comitserver.service;

import com.example.comitserver.dto.CustomUserDetails;
import com.example.comitserver.dto.StudyRequestDTO;
import com.example.comitserver.entity.CreatedStudyEntity;
import com.example.comitserver.entity.StudyEntity;
import com.example.comitserver.entity.UserEntity;
import com.example.comitserver.repository.CreatedStudyRepository;
import com.example.comitserver.repository.StudyRepository;
import com.example.comitserver.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@Transactional
public class StudyService {

    private final StudyRepository studyRepository;
    private final UserRepository userRepository;
    private final CreatedStudyRepository createdStudyRepository;

    public StudyService(StudyRepository studyRepository, UserRepository userRepository, CreatedStudyRepository createdStudyRepository) {
        this.studyRepository = studyRepository;
        this.userRepository = userRepository;
        this.createdStudyRepository = createdStudyRepository;
    }

    public List<StudyEntity> showAllStudies() {
        return studyRepository.findAll();
    }

    public StudyEntity showStudy(Long id) {
        return studyRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Study not found with id: " + id));
    }

    public StudyEntity createStudy(StudyRequestDTO studyRequestDTO, CustomUserDetails customUserDetails) {
        StudyEntity newStudy = new StudyEntity();

        newStudy.setTitle(studyRequestDTO.getTitle());
        newStudy.setImageSrc(studyRequestDTO.getImageSrc());
        // Get the mentor (user) from the repository using the user ID from the customUserDetails
        UserEntity mentor = userRepository.findById(customUserDetails.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + customUserDetails.getUserId()));
        newStudy.setMentor(mentor);
        newStudy.setDayOfWeek(studyRequestDTO.getDayOfWeek());
        newStudy.setStartTime(studyRequestDTO.getStartTime());
        newStudy.setEndTime(studyRequestDTO.getEndTime());
        newStudy.setLevel(studyRequestDTO.getLevel());
        newStudy.setStacks(studyRequestDTO.getStacks());
        newStudy.setLocation(studyRequestDTO.getLocation());
        newStudy.setDescription(studyRequestDTO.getDescription());
        newStudy.setIsRecruiting(studyRequestDTO.getIsRecruiting());
        newStudy.setSemester(studyRequestDTO.getSemester());
        studyRepository.save(newStudy);

        CreatedStudyEntity createdStudy = new CreatedStudyEntity();
        createdStudy.setUser(mentor);
        createdStudy.setStudy(newStudy);
        createdStudyRepository.save(createdStudy);

        return newStudy;

    }

    public StudyEntity updateStudy(Long id, StudyRequestDTO studyRequestDTO) {
        StudyEntity updatingStudy = showStudy(id);

        updatingStudy.setTitle(studyRequestDTO.getTitle());
        updatingStudy.setImageSrc(studyRequestDTO.getImageSrc());
        updatingStudy.setDayOfWeek(studyRequestDTO.getDayOfWeek());
        updatingStudy.setStartTime(studyRequestDTO.getStartTime());
        updatingStudy.setEndTime(studyRequestDTO.getEndTime());
        updatingStudy.setLevel(studyRequestDTO.getLevel());
        updatingStudy.setStacks(studyRequestDTO.getStacks());
        updatingStudy.setLocation(studyRequestDTO.getLocation());
        updatingStudy.setDescription(studyRequestDTO.getDescription());
        updatingStudy.setIsRecruiting(studyRequestDTO.getIsRecruiting());
        updatingStudy.setSemester(studyRequestDTO.getSemester());
        studyRepository.save(updatingStudy);

        return updatingStudy;
    }

    public void deleteStudy(Long id) {
        createdStudyRepository.deleteByStudyId(id);
        StudyEntity deletingStudy = showStudy(id);
        studyRepository.delete(deletingStudy);
    }

    public Boolean identification(Long id, CustomUserDetails customUserDetails) {
        StudyEntity study = showStudy(id);
        Long mentorId = study.getMentor().getId();
        Long requesterId = customUserDetails.getUserId();
        return Objects.equals(requesterId, mentorId);
    }
}
