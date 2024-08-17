package com.example.comitserver.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    @Column(unique = true)
    private String password;
    @Column(unique = true)
    private String phoneNumber;
    @Column(unique = true)
    private String studentId;
    @Column(unique = true)
    private String email;

    private String position;
    private Boolean isStaff;

    @Column(nullable = true)
    private String bio;
    @Column(nullable = true)
    private String github;
    @Column(nullable = true)
    private String blog;
    @Column(nullable = true)
    private String profileImage;

    @Enumerated(EnumType.STRING)
    private Role role;
}