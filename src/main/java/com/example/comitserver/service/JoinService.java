package com.example.comitserver.service;

import com.example.comitserver.dto.JoinDTO;
import com.example.comitserver.entity.Role;
import com.example.comitserver.entity.UserEntity;
import com.example.comitserver.exception.DuplicateResourceException;
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

    public UserEntity joinProcess(JoinDTO joinDTO) {
        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();
        String phoneNumber = joinDTO.getPhoneNumber();
        String studentId = joinDTO.getStudentId();
        String email = joinDTO.getEmail();

        if (userRepository.existsByEmail(joinDTO.getEmail())) {
            throw new DuplicateResourceException("Email is already in use.");
        }
        if (userRepository.existsByStudentId(joinDTO.getStudentId())) {
            throw new DuplicateResourceException("Student ID is already in use.");
        }
        if (userRepository.existsByPhoneNumber(joinDTO.getPhoneNumber())) {
            throw new DuplicateResourceException("Phone number is already in use.");
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

        return data;
    }
}