package com.example.comitserver.controller;

import com.example.comitserver.entity.Role;
import com.example.comitserver.entity.UserEntity;
import com.example.comitserver.service.UserAdminService;
import com.example.comitserver.utils.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class UserAdminController {

    private final UserAdminService userAdminService;

    public UserAdminController(UserAdminService userAdminService) {
        this.userAdminService = userAdminService;
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

        return ResponseUtil.createSuccessResponse(users, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        UserEntity user = userAdminService.getUserById(id);
        return ResponseUtil.createSuccessResponse(user, HttpStatus.OK);
    }

    @PatchMapping("/users/{id}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable Long id, @RequestBody Map<String, String> requestBody) {
        try {
            Role role = Role.valueOf(requestBody.get("role"));
            userAdminService.updateUserRole(id, role);
            return ResponseUtil.createSuccessResponse(null, HttpStatus.NO_CONTENT);
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
            return ResponseUtil.createSuccessResponse(null, HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return ResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST, "Invalid isStaff", "Invalid isStaff value provided: " + requestBody.get("isStaff"));
        }
    }
}