package com.example.comitserver.service;

import com.example.comitserver.config.auth.CustomUserDetails;
import com.example.comitserver.dto.StudyRequestDTO;
import com.example.comitserver.dto.StudyUserDTO;
import com.example.comitserver.entity.*;
import com.example.comitserver.entity.enumeration.Position;
import com.example.comitserver.entity.enumeration.Status;
import com.example.comitserver.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@Transactional
public class StudyService {

    private final StudyRepository studyRepository;
    private final UserRepository userRepository;
    private final StudyUserRepository studyUserRepository;
    private final SemesterRepository semesterRepository;

    public StudyService(StudyRepository studyRepository, UserRepository userRepository, StudyUserRepository studyUserRepository, SemesterRepository semesterRepository) {
        this.studyRepository = studyRepository;
        this.userRepository = userRepository;
        this.studyUserRepository = studyUserRepository;
        this.semesterRepository = semesterRepository;
    }

    public List<Study> showAllStudies() {
        return studyRepository.findAll();
    }

    public Study showStudy(Long id) {
        return studyRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Study not found with id: " + id));
    }

    public Study createStudy(StudyRequestDTO studyRequestDTO, CustomUserDetails customUserDetails) {

        Semester semester = semesterRepository.findById(studyRequestDTO.getSemesterId())
                .orElseThrow(() -> new NoSuchElementException("Semester not found with id: " + studyRequestDTO.getSemesterId()));

        Study newStudy = Study.builder()
                .semester(semester)
                .name(studyRequestDTO.getName())
                .imageSrc(studyRequestDTO.getImageSrc())
                .level(studyRequestDTO.getLevel())
                .capacity(studyRequestDTO.getCapacity())
                .stacks(studyRequestDTO.getStacks())
                .location(studyRequestDTO.getLocation())
                .dayOfWeek(studyRequestDTO.getDayOfWeek())
                .startTime(stringToLocalTime(studyRequestDTO.getStartTime()))
                .endTime(stringToLocalTime(studyRequestDTO.getEndTime()))
                .description(studyRequestDTO.getDescription())
                .status(Status.NOT_STARTED)
                .build();
        studyRepository.save(newStudy);

        User leader = userRepository.findById(customUserDetails.getId())
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + customUserDetails.getId()));
        StudyUser newStudyUser = StudyUser.builder()
                .study(newStudy)
                .user(leader)
                .position(Position.LEADER)
                .build();
        studyUserRepository.save(newStudyUser);

        return newStudy;

    }

    public Study updateStudy(Long id, StudyRequestDTO studyRequestDTO) {
        Study updatingStudy = showStudy(id);

        if (studyRequestDTO.getName() != null) {
            updatingStudy.setName(studyRequestDTO.getName());
        }
        if (studyRequestDTO.getImageSrc() != null) {
            updatingStudy.setImageSrc(studyRequestDTO.getImageSrc());
        }
        if (studyRequestDTO.getLevel() != null) {
            updatingStudy.setLevel(studyRequestDTO.getLevel());
        }
        if (studyRequestDTO.getCapacity() != null) {
            updatingStudy.setCapacity(studyRequestDTO.getCapacity());
        }
        if (studyRequestDTO.getStacks() != null) {
            updatingStudy.setStacks(studyRequestDTO.getStacks());
        }
        if (studyRequestDTO.getLocation() != null) {
            updatingStudy.setLocation(studyRequestDTO.getLocation());
        }
        if (studyRequestDTO.getDayOfWeek() != null) {
            updatingStudy.setDayOfWeek(studyRequestDTO.getDayOfWeek());
        }
        if (studyRequestDTO.getStartTime() != null) {
            updatingStudy.setStartTime(stringToLocalTime(studyRequestDTO.getStartTime()));
        }
        if (studyRequestDTO.getEndTime() != null) {
            updatingStudy.setEndTime(stringToLocalTime(studyRequestDTO.getEndTime()));
        }
        if (studyRequestDTO.getDescription() != null) {
            updatingStudy.setDescription(studyRequestDTO.getDescription());
        }
        if (studyRequestDTO.getStatus() != null) {
            updatingStudy.setStatus(studyRequestDTO.getStatus());
        }
        studyRepository.save(updatingStudy);

        return updatingStudy;
    }

    public void deleteStudy(Long id) {
        studyUserRepository.deleteByStudyId(id);
        Study deletingStudy = showStudy(id);
        studyRepository.delete(deletingStudy);
    }

    public Boolean isLeader(Long id, CustomUserDetails customUserDetails) {
        Long leaderId = -1L;
        List<StudyUser> studyUsers = studyUserRepository.findByStudyId(id);
        for (StudyUser studyUser : studyUsers) {
            if (studyUser.getPosition() == Position.LEADER) {
                leaderId = studyUser.getUser().getId();
                break;
            }
        }
        if (leaderId == -1L) return false;
        Long requesterId = customUserDetails.getId();
        return Objects.equals(requesterId, leaderId);
    }

    public LocalTime stringToLocalTime(String time) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime localTime = LocalTime.parse(time, timeFormatter);
        return localTime;
    }
}
