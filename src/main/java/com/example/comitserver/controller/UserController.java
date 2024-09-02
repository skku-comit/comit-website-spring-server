package com.example.comitserver.controller;

import com.example.comitserver.dto.CustomUserDetails;
import com.example.comitserver.dto.UserDTO;
import com.example.comitserver.entity.StudyEntity;
import com.example.comitserver.entity.UserEntity;
import com.example.comitserver.service.StudyService;
import com.example.comitserver.service.UserService;
import com.example.comitserver.utils.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> getAllStaffProfiles(@RequestParam(required = false) Boolean isStaff,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        if (isStaff != null) {
            List<UserEntity> users = userService.getAllUsersByStaffStatus(isStaff);
            return ResponseUtil.createSuccessResponse(users, HttpStatus.OK);
        }
        Long userId = ((CustomUserDetails) userDetails).getUserId();
        UserEntity userProfile = userService.getCurrentUserProfile(userId);
        return ResponseUtil.createSuccessResponse(userProfile, HttpStatus.OK);
    }

    @GetMapping("/users/profile")
    public ResponseEntity<?> getUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = ((CustomUserDetails) userDetails).getUserId();
        UserEntity userProfile = userService.getUserProfile(userId);
        return ResponseUtil.createSuccessResponse(userProfile, HttpStatus.OK);
    }

    // 업데이트 후 사용자 정보를 반환(200) or 반환하지 않은(204) 중 선택 가능
    @PatchMapping("/users/profile")
    public ResponseEntity<?> updateUserProfile(@AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid UserDTO userDTO) {
        Long userId = ((CustomUserDetails) userDetails).getUserId();
        userService.updateUserProfile(userId, userDTO);
        // 업데이트된 사용자 정보를 반환할 수 있음
        UserEntity updatedProfile = userService.getUserProfile(userId);
        return ResponseUtil.createSuccessResponse(updatedProfile, HttpStatus.OK);
    }
//    @PatchMapping("/users/profile")
//    public ResponseEntity<?> updateUserProfile(@AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid UserDTO userDTO) {
//        Long userId = ((CustomUserDetails) userDetails).getUserId();
//        userService.updateUserProfile(userId, userDTO);
//        return ResponseUtil.createSuccessResponse(null, HttpStatus.NO_CONTENT);
//    }

    @DeleteMapping("/users/profile")
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = ((CustomUserDetails) userDetails).getUserId();
        userService.deleteUser(userId);
        return ResponseUtil.createSuccessResponse(null, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/users/studies/created")
    public ResponseEntity<?> createStudy(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = ((CustomUserDetails) userDetails).getUserId();
        List<StudyEntity> createdStudies = userService.getCreatedStudies(userId);
        return ResponseUtil.createSuccessResponse(createdStudies, HttpStatus.OK);
    }
}