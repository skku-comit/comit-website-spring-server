package com.example.comitserver.dto;

import com.example.comitserver.entity.enumeration.Location;
import com.example.comitserver.entity.enumeration.DayOfWeek;
import com.example.comitserver.entity.enumeration.Level;
import com.example.comitserver.entity.enumeration.Semester;
import lombok.Data;

import java.util.List;

@Data
public class StudyRequestDTO {
    private Long id;
    private String title;
    private String imageSrc;
    private DayOfWeek dayOfWeek;
    private String startTime;
    private String endTime;
    private Level level;
    private List<String> stacks;
    private Location location;
    private String description;
    private Boolean isRecruiting;
    private Semester semester;
}
