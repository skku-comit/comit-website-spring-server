package com.example.comitserver.service;

import com.example.comitserver.dto.JoinDTO;
import com.example.comitserver.entity.Role;
import com.example.comitserver.entity.UserEntity;
import com.example.comitserver.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void joinProcess(JoinDTO joinDTO) {
        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();
        String phoneNumber = joinDTO.getPhoneNumber();
        String studentId = joinDTO.getStudentId();
        String email = joinDTO.getEmail();

        // Check if email is already in use
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Check if student ID is already in use
        if (userRepository.existsByStudentId(studentId)) {
            throw new IllegalArgumentException("Student ID already exists");
        }

        // Check if phone number is already in use
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Phone number already exists");
        }

        UserEntity data = new UserEntity();

        data.setUsername(username);
        data.setPassword(bCryptPasswordEncoder.encode(password));
        data.setPhoneNumber(phoneNumber);
        data.setStudentId(studentId);
        data.setEmail(email);
        data.setRole(Role.ROLE_MEMBER);

        data.setPosition("일반부원");
        data.setIsStaff(false);

        userRepository.save(data);
    }
}