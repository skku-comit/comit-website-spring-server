package com.example.comitserver.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class SignupResponseDTO extends UserResponseDTO {
    private String role;
}
