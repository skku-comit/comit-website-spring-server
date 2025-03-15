package com.example.comitserver.service;

import com.example.comitserver.config.auth.CustomUserDetails;
import com.example.comitserver.dto.StudyUserDTO;
import com.example.comitserver.entity.Study;
import com.example.comitserver.entity.StudyUser;
import com.example.comitserver.entity.User;
import com.example.comitserver.entity.enumeration.Position;
import com.example.comitserver.repository.StudyRepository;
import com.example.comitserver.repository.StudyUserRepository;
import com.example.comitserver.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class StudyUserService {
    private final StudyRepository studyRepository;
    private final UserRepository userRepository;
    private final StudyUserRepository studyUserRepository;

    public StudyUserService(StudyRepository studyRepository, UserRepository userRepository, StudyUserRepository studyUserRepository) {
        this.studyRepository = studyRepository;
        this.userRepository = userRepository;
        this.studyUserRepository = studyUserRepository;
    }

    public StudyUser applyForStudy(Long studyId, CustomUserDetails customUserDetails) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new NoSuchElementException("Study not found with id: " + studyId));
        User applicant = userRepository.findById(customUserDetails.getId())
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + customUserDetails.getId()));
        StudyUser newStudyUser = StudyUser.builder()
                .study(study)
                .user(applicant)
                .position(Position.APPLICANT)
                .build();
        studyUserRepository.save(newStudyUser);

        return newStudyUser;
    }

    public List<StudyUser> showStudyUsers(Long studyId) {
        return studyUserRepository.findByStudyId(studyId);
    }

    public StudyUser changeMemberPosition(StudyUser studyUser, StudyUserDTO studyUserDTO) {
        studyUser.setPosition(studyUserDTO.getPosition());
        studyUserRepository.save(studyUser);

        return studyUser;
    }

    public Boolean isAppliedAlready(Long studyId, CustomUserDetails customUserDetails) {
        User user = userRepository.findById(customUserDetails.getId())
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + customUserDetails.getId()));
        List<StudyUser> studyUsers = studyUserRepository.findByStudyId(studyId);
        for (StudyUser studyUser : studyUsers) {
            if(studyUser.getUser().getId() == user.getId()){
                return true;
            }
        }
        return false;
    }
}
