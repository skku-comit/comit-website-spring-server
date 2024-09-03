package com.example.comitserver.dto;

import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;
    private String username;
    private String phoneNumber;
    private String studentId;
    private String email;
    private String position;
    private String bio;
    private String github;
    private String blog;
    private String profileImage;
}
