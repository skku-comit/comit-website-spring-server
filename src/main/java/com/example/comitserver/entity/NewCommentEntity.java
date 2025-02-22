package com.example.comitserver.entity;

import jakarta.persistence.*;

public class NewCommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private String commentDate;

    @Column(nullable = false)
    private String commentText;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    private NewCourseEntity course;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private NewUserEntity user;
}
