package com.example.comitserver.dto;

import lombok.Data;

@Data
public class JoinResponseDTO extends UserResponseDTO {
    private String role;
}