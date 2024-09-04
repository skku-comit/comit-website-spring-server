package com.example.comitserver.entity;

import com.example.comitserver.entity.enumeration.Campus;
import com.example.comitserver.entity.enumeration.Day;
import com.example.comitserver.entity.enumeration.Level;
import com.example.comitserver.entity.enumeration.Semester;
import com.example.comitserver.utils.StringListConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyEntity extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) // 원래 필드명과 db 열 이름이 같으며 안 적어도 되는데 속성이 있어서 적는 거
    private String title;

    @Column(nullable = false)
    private String imageSrc;

    @ManyToOne(fetch = FetchType.EAGER) // FetchType이 LAZY면 문제가 발생
    @JoinColumn(name = "user_id")
    private UserEntity mentor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Day day;

    @Column(nullable = false)
    private String startTime;

    @Column(nullable = false)
    private String endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Level level;

    @Convert(converter = StringListConverter.class)
    @Column(nullable = false)
    private List<String> stacks;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Campus campus;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Boolean isRecruiting;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Semester semester;

    public void setStacks(List<String> stacks) {
        this.stacks = (stacks != null) ? stacks : new ArrayList<>();
    }
}
