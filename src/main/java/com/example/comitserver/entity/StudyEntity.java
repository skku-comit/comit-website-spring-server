package com.example.comitserver.entity;

import com.example.comitserver.entity.enumeration.Campus;
import com.example.comitserver.entity.enumeration.Day;
import com.example.comitserver.entity.enumeration.Level;
import com.example.comitserver.entity.enumeration.Semester;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @ElementCollection // 배열은 애초에 안 되고 list나 set은 이렇게 따로 collection을 만들어서 관계형으로 설정해야함
    @CollectionTable(name="study_stacks", joinColumns = @JoinColumn(name="study_id"))
    @Column(name="stack", nullable = false)
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
}
