package com.example.comitserver.controller;

import com.example.comitserver.dto.CustomUserDetails;
import com.example.comitserver.dto.UserDTO;
import com.example.comitserver.entity.UserEntity;
import com.example.comitserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Get all staff profiles or current user's profile based on isStaff parameter
    @GetMapping
    public List<UserEntity> getAllStaffProfiles(@RequestParam(required = false) Boolean isStaff) {
        if (isStaff != null) {
            return userService.getAllUsersByStaffStatus(isStaff);
        }
        // Assuming getCurrentUserProfile() returns the current user's profile as a list
        // This can be adjusted if necessary
        return List.of(userService.getCurrentUserProfile());
    }

    // Get user profile by id
    @GetMapping("/profile")
    public UserEntity getUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = ((CustomUserDetails) userDetails).getUserId();
        return userService.getUserProfile(userId);
    }

    // Update user profile
    @PutMapping("/profile")
    public void updateUserProfile(@AuthenticationPrincipal UserDetails userDetails, @RequestBody UserDTO userDTO) {
        Long userId = ((CustomUserDetails) userDetails).getUserId();
        userService.updateUserProfile(userId, userDTO);
    }

    // Delete user
    @DeleteMapping("/profile")
    public void deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = ((CustomUserDetails) userDetails).getUserId();
        userService.deleteUser(userId);
    }
}
