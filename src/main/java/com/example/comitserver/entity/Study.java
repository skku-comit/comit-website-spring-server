package com.example.comitserver.entity;

import com.example.comitserver.entity.enumeration.DayOfWeek;
import com.example.comitserver.entity.enumeration.Location;
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
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER) // FetchType이 LAZY면 문제가 발생
    @JoinColumn(name = "semester_id")
    private Semester semester;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false) // 원래 필드명과 db 열 이름이 같으면 안 적어도 되는데 속성이 있어서 적는 거
    private String name;

    @Column(nullable = false)
    private String imageSrc;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Level level;

    @Column(nullable = false)
    private Integer capacity;

    @Convert(converter = StringListConverter.class)
    @Column(nullable = false)
    private List<String> stacks;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Location location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false, length = 800)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

}
