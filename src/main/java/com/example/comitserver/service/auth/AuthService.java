package com.example.comitserver.service.auth;

import com.example.comitserver.dto.SignupRequestDTO;
import com.example.comitserver.entity.User;
import com.example.comitserver.entity.enumeration.Role;
import com.example.comitserver.exception.DuplicateResourceException;
import com.example.comitserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User signUp(SignupRequestDTO signUpRequestDTO) {
        if (userRepository.existsByEmail(signUpRequestDTO.getEmail())) {
            throw new DuplicateResourceException("Email is already in use.");
        }
        if (userRepository.existsByStudentId(signUpRequestDTO.getStudentId())) {
            throw new DuplicateResourceException("Student ID is already in use.");
        }
        if (userRepository.existsByPhoneNumber(signUpRequestDTO.getPhoneNumber())) {
            throw new DuplicateResourceException("Phone number is already in use.");
        }

        User newUser = User.builder()
                .name(signUpRequestDTO.getName())
                .fullName(signUpRequestDTO.getFullName())
                .password(bCryptPasswordEncoder.encode(signUpRequestDTO.getPassword()))
                .phoneNumber(signUpRequestDTO.getPhoneNumber())
                .studentId(signUpRequestDTO.getStudentId())
                .email(signUpRequestDTO.getEmail())
                .role(Role.ROLE_MEMBER)
                .build();

        if(signUpRequestDTO.getGithub() != null) {
            newUser.setGithub(signUpRequestDTO.getGithub());
        }
        if(signUpRequestDTO.getBlog() != null) {
            newUser.setBlog(signUpRequestDTO.getBlog());
        }

        userRepository.save(newUser);
        return newUser;
    }

}
