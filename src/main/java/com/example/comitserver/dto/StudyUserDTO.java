package com.example.comitserver.dto;

import com.example.comitserver.entity.enumeration.Position;
import lombok.Data;

@Data
public class StudyUserDTO {
    private Long studyId;
    private Long userId;
    private Position position;
}
