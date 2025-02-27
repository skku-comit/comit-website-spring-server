package com.example.comitserver.entity;

import com.example.comitserver.entity.enumeration.Campus;
import com.example.comitserver.entity.enumeration.Level;
import com.example.comitserver.utils.StringListConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Study {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studyId;

    @ManyToOne(fetch = FetchType.EAGER) // FetchType이 LAZY면 문제가 발생
    @JoinColumn(name = "semester_id")
    private Semester semester;

    @Column(nullable = false) // 원래 필드명과 db 열 이름이 같으면 안 적어도 되는데 속성이 있어서 적는 거
    private String studyName;

    @Column(nullable = false)
    private String imageSrc;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Level level;

    @Convert(converter = StringListConverter.class)
    @Column(nullable = false)
    private List<String> stacks;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Campus campus;

    @Column(nullable = false, length = 800)
    private String description;

    @Column(nullable = false)
    private Boolean isRecruiting;

}
