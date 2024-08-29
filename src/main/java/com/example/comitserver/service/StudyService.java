package com.example.comitserver.service;

import com.example.comitserver.dto.StudyDTO;
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

    public StudyEntity createStudy(StudyDTO studyDTO) {
        StudyEntity newStudy = new StudyEntity();

        newStudy.setTitle(studyDTO.getTitle());
        newStudy.setImageSrc(studyDTO.getImageSrc());
        newStudy.setMentor(userRepository.findById(studyDTO.getMentorId()).orElseThrow(()-> new NoSuchElementException("User not found with id: " + studyDTO.getMentorId())));
        newStudy.setDay(studyDTO.getDay());
        newStudy.setStartTime(studyDTO.getStartTime());
        newStudy.setEndTime(studyDTO.getEndTime());
        newStudy.setLevel(studyDTO.getLevel());
        newStudy.setStacks(studyDTO.getStacks());
        newStudy.setCampus(studyDTO.getCampus());
        newStudy.setDescription(studyDTO.getDescription());
        newStudy.setIsRecruiting(studyDTO.getIsRecruiting());
        newStudy.setSemester(studyDTO.getSemester());
        studyRepository.save(newStudy);

        return newStudy;

    }

    public StudyEntity updateStudy(Long id, StudyDTO studyDTO) {
        StudyEntity updatingStudy = showStudy(id);

        updatingStudy.setTitle(studyDTO.getTitle());
        updatingStudy.setImageSrc(studyDTO.getImageSrc());
        updatingStudy.setDay(studyDTO.getDay());
        updatingStudy.setStartTime(studyDTO.getStartTime());
        updatingStudy.setEndTime(studyDTO.getEndTime());
        updatingStudy.setLevel(studyDTO.getLevel());
        updatingStudy.setStacks(studyDTO.getStacks());
        updatingStudy.setCampus(studyDTO.getCampus());
        updatingStudy.setDescription(studyDTO.getDescription());
        updatingStudy.setIsRecruiting(studyDTO.getIsRecruiting());
        updatingStudy.setSemester(studyDTO.getSemester());
        studyRepository.save(updatingStudy);

        return updatingStudy;
    }

    public void deleteStudy(Long id) {
        StudyEntity deletingStudy = showStudy(id);
        studyRepository.delete(deletingStudy);
    }
}
