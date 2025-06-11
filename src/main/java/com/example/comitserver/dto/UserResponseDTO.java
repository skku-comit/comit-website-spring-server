package com.example.comitserver.dto;

import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;
    private String name;
    private String fullName;
    private String phoneNumber;
    private String studentId;
    private String major;
    private String email;
    private String imageSrc;
    private String github;
    private String blog;

}
