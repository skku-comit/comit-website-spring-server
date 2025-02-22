package com.example.comitserver.entity;

import jakarta.persistence.*;

import java.util.List;

public class NewCourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Position position;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "study_id")
    private NewStudyEntity study;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private List<NewUserEntity> users;
}
