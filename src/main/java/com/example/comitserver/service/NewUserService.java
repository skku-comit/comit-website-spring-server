package com.example.comitserver.service;

import com.example.comitserver.dto.UserSignupDTO;
import com.example.comitserver.entity.User;
import com.example.comitserver.entity.enumeration.Role;
import com.example.comitserver.exception.DuplicateResourceException;
import com.example.comitserver.repository.NewDepartmentRepository;
import com.example.comitserver.repository.NewUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class NewUserService {

    private final NewUserRepository newUserRepository;
    private final NewDepartmentRepository newDepartmentRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public NewUserService(NewUserRepository newUserRepository, NewDepartmentRepository newDepartmentRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.newUserRepository = newUserRepository;
        this.newDepartmentRepository = newDepartmentRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User signUp(UserSignupDTO userSignUpDTO) {
        if (newUserRepository.existsByEmail(userSignUpDTO.getEmail())) {
            throw new DuplicateResourceException("Email is already in use.");
        }
        if (newUserRepository.existsByStudentId(userSignUpDTO.getStudentId())) {
            throw new DuplicateResourceException("Student ID is already in use.");
        }
        if (newUserRepository.existsByPhoneNumber(userSignUpDTO.getPhoneNumber())) {
            throw new DuplicateResourceException("Phone number is already in use.");
        }

        User newUser = User.builder()
                .name(userSignUpDTO.getName())
                .fullName(userSignUpDTO.getFullName())
                .password(bCryptPasswordEncoder.encode(userSignUpDTO.getPassword()))
                .phoneNumber(userSignUpDTO.getPhoneNumber())
                .studentId(userSignUpDTO.getStudentId())
                .email(userSignUpDTO.getEmail())
                .role(Role.ROLE_MEMBER)
                .build();

        if(userSignUpDTO.getGithub() != null) {
            newUser.setGithub(userSignUpDTO.getGithub());
        }
        if(userSignUpDTO.getBlog() != null) {
            newUser.setBlog(userSignUpDTO.getBlog());
        }

        newUserRepository.save(newUser);
        return newUser;
    }

    // 검증 함수들

//    public List<User> showStaff() {
//        newDepartmentRepository.findByDepartmentName()
//        newUserRepository.findByTeamsContaining()
//    }
}
