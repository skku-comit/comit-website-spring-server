package com.example.comitserver.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class CreatedStudyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "study_id", nullable = false)
    private StudyEntity study;
}
