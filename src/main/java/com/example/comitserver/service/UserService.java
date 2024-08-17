package com.example.comitserver.service;

import com.example.comitserver.dto.UserDTO;
import com.example.comitserver.entity.UserEntity;
import com.example.comitserver.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserEntity> getAllUsersByStaffStatus(Boolean isStaff) {
        return userRepository.findAllByIsStaff(isStaff);
    }

    public UserEntity getCurrentUserProfile() {
        // Method should return the currently authenticated user's profile
        throw new UnsupportedOperationException("Method not implemented");
    }

    public UserEntity getUserProfile(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
    }

    public void updateUserProfile(Long userId, UserDTO userDTO) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

        // Update user fields with data from UserDTO
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setStudentId(userDTO.getStudentId());
        user.setEmail(userDTO.getEmail());
        user.setBio(userDTO.getBio());
        user.setGithub(userDTO.getGithub());
        user.setBlog(userDTO.getBlog());
        user.setProfileImage(userDTO.getProfileImage());

        userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

        userRepository.delete(user);
    }
}
