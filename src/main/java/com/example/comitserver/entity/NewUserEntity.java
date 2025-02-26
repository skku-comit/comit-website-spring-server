package com.example.comitserver.entity;

import jakarta.persistence.*;

import java.util.List;

public class NewUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @Column(unique = true, nullable = false)
    private String studentId;

    @Column(unique = true, nullable = false)
    private String email;

    @Column
    private String profileImage;

    @Column
    private String github;

    @Column
    private String blog;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "semester_id")
    private List<NewSemesterEntity> semesters;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id")
    private List<NewTeamEntity> teams;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    private List<NewCourseEntity> courses;

}
