package com.example.comitserver.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestDTO {
    // field validity check (임시)
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters.")
    private String username;

    @Pattern(regexp = "\\d{10,15}", message = "Phone number must contain only digits and be between 10 and 15 digits long.")
    private String phoneNumber;

    @Pattern(regexp = "\\d{10}", message = "Student ID must be exactly 10 digits long.")
    private String studentId;

    @Email(message = "Please enter a valid email address.")
    private String email;

    @Size(max = 255, message = "Bio must be less than 255 characters.")
    private String bio;

    @Pattern(regexp = "^(https://github.com/).+", message = "Please enter a valid GitHub URL.")
    private String github;

    @Pattern(regexp = "^(https?://).+", message = "Please enter a valid blog URL.")
    private String blog;

    @Pattern(regexp = "^(https?://).+", message = "Please enter a valid profile image URL.")
    private String profileImage;
}