package com.example.comitserver.service;

import com.example.comitserver.dto.UserDTO;
import com.example.comitserver.entity.UserEntity;
import com.example.comitserver.exception.DuplicateResourceException;
import com.example.comitserver.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserEntity> getAllUsersByStaffStatus(Boolean isStaff) {
        return userRepository.findByIsStaff(isStaff);
    }

    public UserEntity getCurrentUserProfile(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
    }

    public UserEntity getUserProfile(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
    }

    public void updateUserProfile(Long userId, @Valid UserDTO userDTO) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

        // 중복 체크
        checkForDuplicateFields(userDTO, user);
        // 필드별로 null 체크 후 업데이트
        updateUserFields(user, userDTO);

        userRepository.save(user);
    }

    private void checkForDuplicateFields(UserDTO userDTO, UserEntity user) {
        // 이메일 중복 체크 (자신의 이메일은 제외)
        if (userDTO.getEmail() != null && !userDTO.getEmail().equals(user.getEmail()) &&
                userRepository.existsByEmail(userDTO.getEmail())) {
            throw new DuplicateResourceException("Email is already in use.");
        }

        // 학번 중복 체크 (자신의 학번은 제외)
        if (userDTO.getStudentId() != null && !userDTO.getStudentId().equals(user.getStudentId()) &&
                userRepository.existsByStudentId(userDTO.getStudentId())) {
            throw new DuplicateResourceException("Student ID is already in use.");
        }

        // 전화번호 중복 체크 (자신의 전화번호는 제외)
        if (userDTO.getPhoneNumber() != null && !userDTO.getPhoneNumber().equals(user.getPhoneNumber()) &&
                userRepository.existsByPhoneNumber(userDTO.getPhoneNumber())) {
            throw new DuplicateResourceException("Phone number is already in use.");
        }
    }

    private void updateUserFields(UserEntity user, UserDTO userDTO) {
        if (userDTO.getPhoneNumber() != null) {
            user.setPhoneNumber(userDTO.getPhoneNumber());
        }
        if (userDTO.getStudentId() != null) {
            user.setStudentId(userDTO.getStudentId());
        }
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getBio() != null) {
            user.setBio(userDTO.getBio());
        }
        if (userDTO.getGithub() != null) {
            user.setGithub(userDTO.getGithub());
        }
        if (userDTO.getBlog() != null) {
            user.setBlog(userDTO.getBlog());
        }
        if (userDTO.getProfileImage() != null) {
            user.setProfileImage(userDTO.getProfileImage());
        }
    }

    public void deleteUser(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

        userRepository.delete(user);
    }
}
