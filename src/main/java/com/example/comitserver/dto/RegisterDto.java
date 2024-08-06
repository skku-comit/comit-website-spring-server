package com.example.comitserver.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {

    @NotBlank(message = "이름을 입력하세요")
    private String username;

    @NotBlank(message = "비밀번호를 입력하세요")
    private String password;

    @NotBlank(message = "이메일을 입력하세요")
    @Email(message = "유효한 이메일 주소를 입력하세요")
    private String email;

    // Additional fields, if needed
    @NotBlank(message = "비밀번호 확인을 입력하세요")
    private String confirmPassword;

    // You can add more fields like terms acceptance, etc.
}