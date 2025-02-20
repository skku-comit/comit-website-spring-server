package com.example.comitserver.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class NewSemesterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long semesterId;

    @Column(nullable = false)
    private String semesterName;

    @Column(nullable = false)
    private String startDate;

    @Column(nullable = false)
    private String endDate;
}
