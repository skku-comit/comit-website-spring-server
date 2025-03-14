package com.example.comitserver.dto;

import com.example.comitserver.entity.enumeration.*;
import lombok.Data;

import java.util.List;

@Data
public class StudyResponseDTO {
    private Long id;
    private Semester semester;
    private String name;
    private String imageSrc;
    private Level level;
    private Integer capacity;
    private List<String> stacks;
    private Location location;
    private DayOfWeek dayOfWeek;
    private String startTime;
    private String endTime;
    private String description;
    private Status status;
}
