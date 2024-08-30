package com.example.comitserver.controller;

import com.example.comitserver.dto.CustomUserDetails;
import com.example.comitserver.dto.UserDTO;
import com.example.comitserver.entity.UserEntity;
import com.example.comitserver.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<UserEntity> getAllStaffProfiles(@RequestParam(required = false) Boolean isStaff,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        if (isStaff != null) {
            return userService.getAllUsersByStaffStatus(isStaff);
        }
        Long userId = ((CustomUserDetails) userDetails).getUserId();
        return List.of(userService.getCurrentUserProfile(userId));
    }

    @GetMapping("/users/profile")
    public UserEntity getUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = ((CustomUserDetails) userDetails).getUserId();
        return userService.getUserProfile(userId);
    }

    // put->patch 변경
    @PatchMapping("/users/profile")
    public void updateUserProfile(@AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid UserDTO userDTO) {
        Long userId = ((CustomUserDetails) userDetails).getUserId();
        userService.updateUserProfile(userId, userDTO);
    }

    @DeleteMapping("/users/profile")
    public void deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = ((CustomUserDetails) userDetails).getUserId();
        userService.deleteUser(userId);
    }
}
