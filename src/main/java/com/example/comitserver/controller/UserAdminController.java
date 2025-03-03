package com.example.comitserver.controller;

import com.example.comitserver.dto.AdminUserResponseDTO;
import com.example.comitserver.entity.enumeration.Role;
import com.example.comitserver.entity.UserEntity;
import com.example.comitserver.service.UserAdminService;
import com.example.comitserver.utils.ResponseUtil;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class UserAdminController {

    private final UserAdminService userAdminService;
    private final ModelMapper modelMapper;

    public UserAdminController(UserAdminService userAdminService, ModelMapper modelMapper) {
        this.userAdminService = userAdminService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestParam(required = false) Boolean isStaff,
                                         @RequestParam(required = false) Role role) {
        List<UserEntity> users;

        if (isStaff != null && role != null) {
            users = userAdminService.getUsersByRoleAndIsStaff(role, isStaff);
        } else if (isStaff != null) {
            users = userAdminService.getUsersByIsStaff(isStaff);
        } else if (role != null) {
            users = userAdminService.getUsersByRole(role);
        } else {
            users = userAdminService.getAllUsers();
        }

        List<AdminUserResponseDTO> userDTOs = users.stream().map(entity -> modelMapper.map(entity, AdminUserResponseDTO.class)).toList();

        return ResponseUtil.createSuccessResponse(userDTOs, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        UserEntity user = userAdminService.getUserById(id);
        AdminUserResponseDTO userDTO = modelMapper.map(user, AdminUserResponseDTO.class);
        return ResponseUtil.createSuccessResponse(userDTO, HttpStatus.OK);
    }

    @PatchMapping("/users/{id}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable Long id, @RequestBody Map<String, String> requestBody) {
        Role role = Role.valueOf(requestBody.get("role"));
        userAdminService.updateUserRole(id, role);
        UserEntity user = userAdminService.getUserById(id);
        AdminUserResponseDTO userDTO = modelMapper.map(user, AdminUserResponseDTO.class);
        return ResponseUtil.createSuccessResponse(userDTO, HttpStatus.OK);
    }

    @PatchMapping("/users/{id}/isStaff")
    public ResponseEntity<?> updateUserIsStaff(@PathVariable Long id, @RequestBody Map<String, Boolean> requestBody) {
        Boolean isStaff = requestBody.get("isStaff");

        // 엔티티에서 nullable하지 않아 발생할 수 있는 500 Internal Server Error를 방지하고
        // 클라이언트에게 400 Bad Request 응답을 반환하도록 설정
        if (isStaff == null) {
            throw new IllegalArgumentException("isStaff value must be provided and must be either true or false.");
        }
        userAdminService.updateUserIsStaff(id, isStaff);
        UserEntity user = userAdminService.getUserById(id);
        AdminUserResponseDTO userDTO = modelMapper.map(user, AdminUserResponseDTO.class);
        return ResponseUtil.createSuccessResponse(userDTO, HttpStatus.OK);
    }

    @PatchMapping("/users/{id}/position")
    public ResponseEntity<?> updateUserPosition(@PathVariable Long id, @RequestBody Map<String, String> requestBody) {
        String position = requestBody.get("position");

        // 엔티티에서 nullable하지 않아 발생할 수 있는 500 Internal Server Error를 방지하고
        // 클라이언트에게 400 Bad Request 응답을 반환하도록 설정
        if (position == null) {
            throw new IllegalArgumentException("position value must be provided.");
        }
        userAdminService.updateUserPosition(id, position);
        UserEntity user = userAdminService.getUserById(id);
        AdminUserResponseDTO userDTO = modelMapper.map(user, AdminUserResponseDTO.class);
        return ResponseUtil.createSuccessResponse(userDTO, HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        UserEntity user = userAdminService.getUserById(id); // 존재하지 않을 때 이미 GlobalExceptionHandler에서 처리됨
        boolean deleted = userAdminService.deleteUserById(id);

        // 삭제 실패 시, Internal Server Error 응답 반환
        if (!deleted) {
            return ResponseUtil.createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Deletion Failed", "Failed to delete user with id: " + id);
        } else {
            // 삭제 성공 시, OK 응답 반환
            AdminUserResponseDTO userDTO = modelMapper.map(user, AdminUserResponseDTO.class);
            return ResponseUtil.createSuccessResponse(userDTO, HttpStatus.OK);
        }
    }
}