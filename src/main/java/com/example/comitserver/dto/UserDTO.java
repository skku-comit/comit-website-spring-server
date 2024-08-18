package com.example.comitserver.dto;

import com.example.comitserver.entity.Role;
import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String phoneNumber;
    private String studentId;
    private String email;
    private String bio;
    private String github;
    private String blog;
    private String profileImage;
}
