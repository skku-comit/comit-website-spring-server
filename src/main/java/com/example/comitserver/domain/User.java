package com.example.comitserver.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data // lombok에 의해 자동으로 getter/setter 생성
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id; // unique

    @Column(name = "name", nullable = false)
    private String name;
    private String studentId; // unique
    private String email; // unique
    private String password;

    private String bio;
    private String github;
    private String blog;
    private String profileImage;
    private boolean isStaff; // setStaff
    private String position;

    @Enumerated(EnumType.STRING)
    private Access access;

    public enum Access {
        MEMBER, VERIFIED, ADMIN
    }

}
