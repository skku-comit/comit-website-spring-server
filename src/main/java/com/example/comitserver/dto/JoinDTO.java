package com.example.comitserver.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class JoinDTO {
    @NotBlank(message = "Username is required.")
    private String username;

    @NotBlank(message = "Password is required.")
    @Size(min = 6, message = "Password must be at least 6 characters long.")
    private String password;

    @NotBlank(message = "Phone number is required.")
    @Pattern(regexp = "\\d+", message = "Phone number must contain only digits.")
    private String phoneNumber;

    @NotBlank(message = "Student ID is required.")
    @Pattern(regexp = "\\d{10}", message = "Student ID must be exactly 10 digits long.")
    private String studentId;

    @NotBlank(message = "Email is required.")
    @Email(message = "Please enter a valid email address.")
    private String email;
}
