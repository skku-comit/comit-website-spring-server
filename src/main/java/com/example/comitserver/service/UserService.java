package com.example.comitserver.service;

import com.example.comitserver.dto.UserRequestDTO;
import com.example.comitserver.entity.User;
import com.example.comitserver.exception.DuplicateResourceException;
import com.example.comitserver.exception.ResourceNotFoundException;
import com.example.comitserver.repository.StudyUserRepository;
import com.example.comitserver.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final StudyUserRepository studyUserRepository;

    @Autowired
    public UserService(UserRepository userRepository, StudyUserRepository studyUserRepository) {
        this.userRepository = userRepository;
        this.studyUserRepository = studyUserRepository;
    }

    public User showProfile(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    public void updateProfile(Long userId, @Valid UserRequestDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // @Valid 가 UserDto의 필드 validity check
        checkDuplicateFields(userDTO, user);
        updateUserFields(user, userDTO);

        userRepository.save(user);
    }

    private void checkDuplicateFields(UserRequestDTO userDTO, User user) {
        if (userDTO.getEmail() != null && !userDTO.getEmail().equals(user.getEmail()) &&
                userRepository.existsByEmail(userDTO.getEmail())) {
            throw new DuplicateResourceException("Email is already in use.");
        }

        // 학번은 바꾸면 안 되지
//        if (userDTO.getStudentId() != null && !userDTO.getStudentId().equals(user.getStudentId()) &&
//                userRepository.existsByStudentId(userDTO.getStudentId())) {
//            throw new DuplicateResourceException("Student ID is already in use.");
//        }

        if (userDTO.getPhoneNumber() != null && !userDTO.getPhoneNumber().equals(user.getPhoneNumber()) &&
                userRepository.existsByPhoneNumber(userDTO.getPhoneNumber())) {
            throw new DuplicateResourceException("Phone number is already in use.");
        }
    }

    private void updateUserFields(User user, UserRequestDTO userDTO) {
        if (userDTO.getMajor() != null) {
            user.setMajor(userDTO.getMajor());
        }
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
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return true;
        } else {
            return false;
        }
    }


//    public List<Study> getCreatedStudies(Long userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));
//
//        List<StudyUser> createdStudies = studyUserRepository.findByUserId(userId);
//
//        // Extract and return the list of Study objects from StudyUser
//        return createdStudies.stream()
//                .map(StudyUser::getStudy)
//                .collect(Collectors.toList());
//    }
}
