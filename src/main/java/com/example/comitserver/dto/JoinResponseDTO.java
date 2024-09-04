package com.example.comitserver.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class JoinResponseDTO extends UserResponseDTO {
    private String role;
}