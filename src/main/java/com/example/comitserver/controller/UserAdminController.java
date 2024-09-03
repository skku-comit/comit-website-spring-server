package com.example.comitserver.controller;

import com.example.comitserver.dto.AdminUserResponseDTO;
import com.example.comitserver.entity.Role;
import com.example.comitserver.entity.UserEntity;
import com.example.comitserver.service.UserAdminService;
import com.example.comitserver.utils.ResponseUtil;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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
        try {
            Role role = Role.valueOf(requestBody.get("role"));
            userAdminService.updateUserRole(id, role);
            AdminUserResponseDTO userDTO = modelMapper.map(userAdminService.getUserById(id), AdminUserResponseDTO.class);
            return ResponseUtil.createSuccessResponse(userDTO, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST, "Invalid Role", "Invalid role value provided: " + requestBody.get("role"));
        }
    }

    @PatchMapping("/users/{id}/isStaff")
    public ResponseEntity<?> updateUserIsStaff(@PathVariable Long id, @RequestBody Map<String, Boolean> requestBody) {
        try {
            Boolean isStaff = requestBody.get("isStaff");

            if (isStaff == null) {
                return ResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST, "Invalid isStaff", "isStaff value must be provided and must be either true or false.");
            }

            userAdminService.updateUserIsStaff(id, isStaff);

            AdminUserResponseDTO userDTO = modelMapper.map(userAdminService.getUserById(id), AdminUserResponseDTO.class);
            return ResponseUtil.createSuccessResponse(userDTO, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST, "Invalid isStaff", "Invalid isStaff value provided: " + requestBody.get("isStaff"));
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        try {
            UserEntity user = userAdminService.getUserById(id);
            boolean deleted = userAdminService.deleteUserById(id);

            if (deleted) {
                AdminUserResponseDTO userDTO = modelMapper.map(user, AdminUserResponseDTO.class);
                return ResponseUtil.createSuccessResponse(userDTO, HttpStatus.OK);
            } else {
                return ResponseUtil.createErrorResponse(HttpStatus.NOT_FOUND, "User Not Found", "No user found with id: " + id);
            }
        } catch (NoSuchElementException e) {
            return ResponseUtil.createErrorResponse(HttpStatus.NOT_FOUND, "User Not Found", "No user found with id: " + id);
        } catch (Exception e) {
            return ResponseUtil.createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Deletion Failed", "An error occurred while attempting to delete the user.");
        }
    }
}