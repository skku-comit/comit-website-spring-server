package com.example.comitserver.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "study_id")
    private Study study;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private LocalDate postDate;

    @Column(nullable = false)
    private String postTitle;

    @Column(nullable = false)
    private String postLink;

}
