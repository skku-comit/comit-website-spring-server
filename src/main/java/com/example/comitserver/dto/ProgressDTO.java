package com.example.comitserver.dto;

import com.example.comitserver.entity.enumeration.Status;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

@Data
public class ProgressDTO {
    private Long id;
    private Long studyId;
    private Integer week;
    private String title;
    private String content;
    private Status status;

}
