package com.example.comitserver.dto;

import com.example.comitserver.entity.enumeration.Campus;
import com.example.comitserver.entity.enumeration.Day;
import com.example.comitserver.entity.enumeration.Level;
import com.example.comitserver.entity.enumeration.Semester;
import lombok.Data;

import java.util.List;

@Data
public class StudyDTO {
    private Long id;
    private String title;
    private String imageSrc;
    private String mentor;
    private Day day;
    private String startTime;
    private String endTime;
    private Level level;
    private List<String> stacks;
    private Campus campus;
    private String description;
    private Boolean isRecruiting;
    private Semester semester;
}
