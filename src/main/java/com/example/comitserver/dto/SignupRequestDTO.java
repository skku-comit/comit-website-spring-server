package com.example.comitserver.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequestDTO {

    @NotBlank(message = "Name(ID) is required.")
    @Size(min = 3, max = 20, message = "Name(ID) must be between 3 and 20 characters.")
    private String name;

    @NotBlank(message = "Full name is required.")
    @Size(min = 2, max = 10, message = "FullName must be between 2 and 10 characters.")
    private String fullName;

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

    private String github;

    private String blog;
}
