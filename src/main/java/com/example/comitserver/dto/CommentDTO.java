package com.example.comitserver.dto;

import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    private Long studyId;
    private Long userId;
    private String dateTime;
    private String text;
}
