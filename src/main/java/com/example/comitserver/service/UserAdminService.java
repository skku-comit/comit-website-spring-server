package com.example.comitserver.service;

import com.example.comitserver.entity.Role;
import com.example.comitserver.entity.UserEntity;
import com.example.comitserver.exception.ResourceNotFoundException;
import com.example.comitserver.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAdminService {

    private final UserRepository userRepository;

    public UserAdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public List<UserEntity> getUsersByIsStaff(Boolean isStaff) {
        return userRepository.findByIsStaff(isStaff);
    }

    public List<UserEntity> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public List<UserEntity> getUsersByRoleAndIsStaff(Role role, Boolean isStaff) {
        return userRepository.findByRoleAndIsStaff(role, isStaff);
    }

    public UserEntity getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public void updateUserRole(Long id, Role role) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setRole(role);
        userRepository.save(user);
    }

    public void updateUserIsStaff(Long id, Boolean isStaff) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setIsStaff(isStaff);
        userRepository.save(user);
    }
}
