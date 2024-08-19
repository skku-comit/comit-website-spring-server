package com.example.comitserver.dto;

import lombok.Data;

@Data
public class ServerErrorDTO {
    private String errorType;
    private String title;
    private String detail;

    public ServerErrorDTO(String errorType, String title, String detail) {
        this.errorType = errorType;
        this.title = title;
        this.detail = detail;
    }
}
