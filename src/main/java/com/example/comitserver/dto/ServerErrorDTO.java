package com.example.comitserver.dto;

import lombok.Data;

@Data
public class ServerErrorDTO {
    private String errorType;
    private String detail;

    public ServerErrorDTO(String errorType, String detail) {
        this.errorType = errorType;
        this.detail = detail;
    }
}
