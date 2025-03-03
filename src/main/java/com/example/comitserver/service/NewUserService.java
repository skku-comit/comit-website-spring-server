package com.example.comitserver.service;

import com.example.comitserver.dto.UserSignUpDTO;
import com.example.comitserver.entity.UserEntity;
import com.example.comitserver.repository.NewDepartmentRepository;
import com.example.comitserver.repository.NewUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewUserService {

    private final NewUserRepository newUserRepository;
    private final NewDepartmentRepository newDepartmentRepository;

    @Autowired
    public NewUserService(NewUserRepository newUserRepository, NewDepartmentRepository newDepartmentRepository) {
        this.newUserRepository = newUserRepository;
        this.newDepartmentRepository = newDepartmentRepository;
    }

    public UserEntity signUp(UserSignUpDTO userSignUpDTO) {

    }

    // 검증 함수들

//    public List<User> showStaff() {
//        newDepartmentRepository.findByDepartmentName()
//        newUserRepository.findByTeamsContaining()
//    }
}
