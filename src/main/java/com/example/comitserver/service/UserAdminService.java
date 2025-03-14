//package com.example.comitserver.service;
//
//import com.example.comitserver.entity.User;
//import com.example.comitserver.entity.enumeration.Role;
//import com.example.comitserver.exception.ResourceNotFoundException;
//import com.example.comitserver.repository.UserRepository;
//import com.example.comitserver.repository.StudyUserRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class UserAdminService {
//
//    private final UserRepository newUserRepository;
//    private final StudyUserRepository studyUserRepository;
//
//    public UserAdminService(UserRepository newUserRepository, StudyUserRepository studyUserRepository) {
//        this.newUserRepository = newUserRepository;
//        this.studyUserRepository = studyUserRepository;
//    }
//
//    public List<User> getAllUsers() {
//        return newUserRepository.findAll();
//    }
//
//    public User getUserById(Long id) {
//        return newUserRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
//    }
//
//    public void updateUserRole(Long id, Role role) {
//        User user = newUserRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
//        user.setRole(role);
//        newUserRepository.save(user);
//    }
//
//}
