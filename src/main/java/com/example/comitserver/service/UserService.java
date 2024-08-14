package com.example.comitserver.service;

import com.example.comitserver.domain.User;
import com.example.comitserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // create
    public User createUser(User user) {
        user.setAccess(User.Access.MEMBER); // 기본 접근 권한 설정
        user.setStaff(false); // 기본적으로 staff가 아닌 일반 회원으로 설정
        user.setPosition("일반부원"); // 기본 포지션 설정
        user.setAccess(User.Access.MEMBER);
        return userRepository.save(user);
    }

    // research
    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }

    public List<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    public Optional<User> findByStudentId(String studentId) {
        return userRepository.findByStudentId(studentId);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public List<User> findAllStaff() {
        return userRepository.findByIsStaff(true);
    }

    // update
    public User updateUserStaffStatus(Long userId, boolean isStaff) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setStaff(isStaff);
            return userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
    }

    public User updateUserAccess(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setAccess(User.Access.VERIFIED);
            return userRepository.save(user);
        } else{
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
    }

    public User updateUser(Long userId, String name, String studentId, String email, String password) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (name != null) {
                user.setName(name);
            }
            if (studentId != null) {
                user.setStudentId(studentId);
            }
            if (email != null) {
                user.setEmail(email);
            }
            if (password != null) {
                user.setPassword(password);
            }

            return userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }

    }


    // delete
    public void delete(User user) {
        userRepository.delete(user);
    }

    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }


}