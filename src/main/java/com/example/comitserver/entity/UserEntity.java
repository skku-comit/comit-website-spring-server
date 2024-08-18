package com.example.comitserver.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String phoneNumber;
    @Column(unique = true, nullable = false)
    private String studentId;
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String position;
    @Column(nullable = false)
    private Boolean isStaff;

    @Column()
    private String bio;
    @Column()
    private String github;
    @Column()
    private String blog;
    @Column()
    private String profileImage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}