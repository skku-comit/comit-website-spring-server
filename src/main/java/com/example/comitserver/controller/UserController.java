package com.example.comitserver.controller;

import com.example.comitserver.domain.User;
import com.example.comitserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/staff")
    public List<User> getAllStaff() {
        return userService.findAllStaff();
    }

    @GetMapping
    public List<User> getUsersByAccess(@RequestParam(required = false) String access) {
        if ("staff".equalsIgnoreCase(access)) {
            return userService.findAllStaff();
        } else {
            return userService.findAllUsers();
        }
    }

}