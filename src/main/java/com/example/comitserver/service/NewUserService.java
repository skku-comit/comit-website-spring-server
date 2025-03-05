package com.example.comitserver.service;

import com.example.comitserver.dto.SignupRequestDTO;
import com.example.comitserver.entity.User;
import com.example.comitserver.entity.enumeration.Role;
import com.example.comitserver.exception.DuplicateResourceException;
import com.example.comitserver.repository.NewUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class NewUserService {

    private final NewUserRepository newUserRepository;

    @Autowired
    public NewUserService(NewUserRepository newUserRepository) {
        this.newUserRepository = newUserRepository;
    }


    // 검증 함수들

//    public List<User> showStaff() {
//        newDepartmentRepository.findByDepartmentName()
//        newUserRepository.findByTeamsContaining()
//    }
}
