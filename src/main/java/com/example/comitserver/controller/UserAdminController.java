package com.example.comitserver.controller;

import com.example.comitserver.entity.Role;
import com.example.comitserver.entity.UserEntity;
import com.example.comitserver.service.UserAdminService;
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
    public List<UserEntity> getAllUsers(@RequestParam(required = false) Boolean isStaff,
                                        @RequestParam(required = false) Role role) {
        if (isStaff != null && role != null) {
            return userAdminService.getUsersByRoleAndIsStaff(role, isStaff);
        } else if (isStaff != null) {
            return userAdminService.getUsersByIsStaff(isStaff);
        } else if (role != null) {
            return userAdminService.getUsersByRole(role);
        }
        return userAdminService.getAllUsers();
    }


    @GetMapping("/users/{id}")
    public UserEntity getUserById(@PathVariable Long id) {
        return userAdminService.getUserById(id);
    }

    @PatchMapping("/users/{id}/role")
    public void updateUserRole(@PathVariable Long id, @RequestBody Map<String, String> requestBody) {
        try {
            Role role = Role.valueOf(requestBody.get("role"));
            userAdminService.updateUserRole(id, role);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role value provided: " + requestBody.get("role"));
        }
    }

    @PatchMapping("/users/{id}/isStaff")
    public void updateUserIsStaff(@PathVariable Long id, @RequestBody Map<String, Boolean> requestBody) {
        try {
            Boolean isStaff = requestBody.get("isStaff");

            if (isStaff == null) {
                throw new IllegalArgumentException("isStaff value must be provided and must be either true or false.");
            }

            userAdminService.updateUserIsStaff(id, isStaff);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid isStaff value provided: " + requestBody.get("isStaff"));
        }
    }
}