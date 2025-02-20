package com.example.comitserver.entity;

import jakarta.persistence.*;

public class NewPostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = false)
    private String postDate;

    @Column(nullable = false)
    private String postName;

    @Column(nullable = false)
    private String postLink;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    private NewCourseEntity course;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
