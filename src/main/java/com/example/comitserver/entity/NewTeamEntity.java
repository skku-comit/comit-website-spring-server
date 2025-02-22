package com.example.comitserver.entity;

import jakarta.persistence.*;

import java.util.List;

public class NewTeamEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Position position;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    private NewDepartmentEntity department;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private List<NewUserEntity> users;
}
