package com.example.comitserver.service;

import com.example.comitserver.dto.UserRequestDTO;
import com.example.comitserver.entity.CreatedStudyEntity;
import com.example.comitserver.entity.StudyEntity;
import com.example.comitserver.entity.User;
import com.example.comitserver.entity.UserEntity;
import com.example.comitserver.exception.DuplicateResourceException;
import com.example.comitserver.exception.ResourceNotFoundException;
import com.example.comitserver.repository.NewUserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class NewUserService {

    private final NewUserRepository newUserRepository;

    @Autowired
    public NewUserService(NewUserRepository newUserRepository) {
        this.newUserRepository = newUserRepository;
    }

    public User showProfile(Long userId) {
        return newUserRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    public void updateProfile(Long userId, @Valid UserRequestDTO userDTO) {
        User user = newUserRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // @Valid 가 UserDto의 필드 validity check
        checkDuplicateFields(userDTO, user);
        updateUserFields(user, userDTO);

        newUserRepository.save(user);
    }

    private void checkDuplicateFields(UserRequestDTO userDTO, User user) {
        if (userDTO.getEmail() != null && !userDTO.getEmail().equals(user.getEmail()) &&
                newUserRepository.existsByEmail(userDTO.getEmail())) {
            throw new DuplicateResourceException("Email is already in use.");
        }

        // 학번은 바꾸면 안 되지
//        if (userDTO.getStudentId() != null && !userDTO.getStudentId().equals(user.getStudentId()) &&
//                newUserRepository.existsByStudentId(userDTO.getStudentId())) {
//            throw new DuplicateResourceException("Student ID is already in use.");
//        }

        if (userDTO.getPhoneNumber() != null && !userDTO.getPhoneNumber().equals(user.getPhoneNumber()) &&
                newUserRepository.existsByPhoneNumber(userDTO.getPhoneNumber())) {
            throw new DuplicateResourceException("Phone number is already in use.");
        }
    }

    private void updateUserFields(User user, UserRequestDTO userDTO) {
        if (userDTO.getPhoneNumber() != null) {
            user.setPhoneNumber(userDTO.getPhoneNumber());
        }
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getImageSrc() != null) {
            user.setImageSrc(userDTO.getImageSrc());
        }
        if (userDTO.getGithub() != null) {
            user.setGithub(userDTO.getGithub());
        }
        if (userDTO.getBlog() != null) {
            user.setBlog(userDTO.getBlog());
        }
    }

    public boolean deleteUser(Long userId) {
        if (newUserRepository.existsById(userId)) {
            newUserRepository.deleteById(userId);
            return true;
        } else {
            return false;
        }
    }

//    public List<User> showStaff() {
//        newDepartmentRepository.findByDepartmentName()
//        newUserRepository.findByTeamsContaining()
//    }

//    public List<StudyEntity> getCreatedStudies(Long userId) {
//        UserEntity user = userRepository.findById(userId)
//                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));
//
//        List<CreatedStudyEntity> createdStudies = createdStudyRepository.findByUser(user);
//
//        // Extract and return the list of StudyEntity objects from CreatedStudyEntity
//        return createdStudies.stream()
//                .map(CreatedStudyEntity::getStudy)
//                .collect(Collectors.toList());
//    }
}
