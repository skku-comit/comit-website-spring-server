package com.example.comitserver.service;

import com.example.comitserver.dto.CustomUserDetails;
import com.example.comitserver.dto.StudyRequestDTO;
import com.example.comitserver.dto.StudyResponseDTO;
import com.example.comitserver.entity.StudyEntity;
import com.example.comitserver.repository.StudyRepository;
import com.example.comitserver.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class StudyService {

    private final StudyRepository studyRepository;
    private final UserRepository userRepository;

    public StudyService(StudyRepository studyRepository, UserRepository userRepository) {
        this.studyRepository = studyRepository;
        this.userRepository = userRepository;
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
       // header의 token에서 user id 가져와서 findByID해서 mentor 넣어야 함
        newStudy.setMentor(userRepository.findById(customUserDetails.getUserId()).orElseThrow(() -> new NoSuchElementException("User not found with id: " + customUserDetails.getUserId())));
        newStudy.setDay(studyRequestDTO.getDay());
        newStudy.setStartTime(studyRequestDTO.getStartTime());
        newStudy.setEndTime(studyRequestDTO.getEndTime());
        newStudy.setLevel(studyRequestDTO.getLevel());
        newStudy.setStacks(studyRequestDTO.getStacks());
        newStudy.setCampus(studyRequestDTO.getCampus());
        newStudy.setDescription(studyRequestDTO.getDescription());
        newStudy.setIsRecruiting(studyRequestDTO.getIsRecruiting());
        newStudy.setSemester(studyRequestDTO.getSemester());
        studyRepository.save(newStudy);

        return newStudy;

    }

    public StudyEntity updateStudy(Long id, StudyRequestDTO studyRequestDTO) {
        StudyEntity updatingStudy = showStudy(id);

        updatingStudy.setTitle(studyRequestDTO.getTitle());
        updatingStudy.setImageSrc(studyRequestDTO.getImageSrc());
        updatingStudy.setDay(studyRequestDTO.getDay());
        updatingStudy.setStartTime(studyRequestDTO.getStartTime());
        updatingStudy.setEndTime(studyRequestDTO.getEndTime());
        updatingStudy.setLevel(studyRequestDTO.getLevel());
        updatingStudy.setStacks(studyRequestDTO.getStacks());
        updatingStudy.setCampus(studyRequestDTO.getCampus());
        updatingStudy.setDescription(studyRequestDTO.getDescription());
        updatingStudy.setIsRecruiting(studyRequestDTO.getIsRecruiting());
        updatingStudy.setSemester(studyRequestDTO.getSemester());
        studyRepository.save(updatingStudy);

        return updatingStudy;
    }

    public void deleteStudy(Long id) {
        StudyEntity deletingStudy = showStudy(id);
        studyRepository.delete(deletingStudy);
    }
}
