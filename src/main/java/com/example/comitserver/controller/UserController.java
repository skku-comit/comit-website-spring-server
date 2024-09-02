package com.example.comitserver.controller;

import com.example.comitserver.dto.CustomUserDetails;
import com.example.comitserver.dto.StudyResponseDTO;
import com.example.comitserver.dto.UserRequestDTO;
import com.example.comitserver.dto.UserResponseDTO;
import com.example.comitserver.entity.StudyEntity;
import com.example.comitserver.entity.UserEntity;
import com.example.comitserver.service.UserService;
import com.example.comitserver.utils.ResponseUtil;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/staffs")
    public ResponseEntity<?> getAllStaffProfiles() {
        List<UserEntity> users = userService.getAllUsersByStaffStatus(true);
        List<UserResponseDTO> userDTOs = users.stream().map(entity -> modelMapper.map(entity, UserResponseDTO.class)).toList();
        return ResponseUtil.createSuccessResponse(userDTOs, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = ((CustomUserDetails) userDetails).getUserId();
        UserEntity userProfile = userService.getUserProfile(userId);
        UserResponseDTO userDTO = modelMapper.map(userProfile, UserResponseDTO.class);
        return ResponseUtil.createSuccessResponse(userDTO, HttpStatus.OK);
    }

    // 업데이트 후 사용자 정보를 반환(200) or 반환하지 않은(204) 중 선택 가능
    @PatchMapping("/profile")
    public ResponseEntity<?> updateUserProfile(@AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid UserRequestDTO userDTO) {
        Long userId = ((CustomUserDetails) userDetails).getUserId();
        userService.updateUserProfile(userId, userDTO);
        // 업데이트된 사용자 정보를 반환할 수 있음
        UserEntity updatedProfile = userService.getUserProfile(userId);
        UserResponseDTO updatedUserDTO = modelMapper.map(updatedProfile, UserResponseDTO.class);
        return ResponseUtil.createSuccessResponse(updatedUserDTO, HttpStatus.OK);
    }
//    @PatchMapping("/profile")
//    public ResponseEntity<?> updateUserProfile(@AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid UserDTO userDTO) {
//        Long userId = ((CustomUserDetails) userDetails).getUserId();
//        userService.updateUserProfile(userId, userDTO);
//        return ResponseUtil.createSuccessResponse(null, HttpStatus.NO_CONTENT);
//    }

    @DeleteMapping("/profile")
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = ((CustomUserDetails) userDetails).getUserId();
        userService.deleteUser(userId);
        return ResponseUtil.createSuccessResponse(null, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/profile/studies-created")
    public ResponseEntity<?> createStudy(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = ((CustomUserDetails) userDetails).getUserId();
        List<StudyEntity> createdStudies = userService.getCreatedStudies(userId);
        List<StudyResponseDTO> studyDTOs = createdStudies.stream().map(entity -> modelMapper.map(entity, StudyResponseDTO.class)).toList();
        return ResponseUtil.createSuccessResponse(studyDTOs, HttpStatus.OK);
    }
}