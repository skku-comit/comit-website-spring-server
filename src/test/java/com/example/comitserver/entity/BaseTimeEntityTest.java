package com.example.comitserver.entity;

import com.example.comitserver.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//@Transactional
@EnableJpaAuditing
class BaseTimeEntityTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void BaseTimeEntity_등록() throws Exception{

        //given
        LocalDateTime now = LocalDateTime.of(2024,8,20,0,0,0);

        UserEntity user1 = UserEntity.builder()
                .email("test@test.com")
                .password("123456")
                .username("test")
                .phoneNumber("010-1234-5678")
                .studentId("20240001")
                .position("Student")
                .isStaff(false)
                .role(Role.ROLE_MEMBER)
                .build();

        userRepository.save(user1);

        //when
        List<UserEntity> usersList = userRepository.findAll();
        UserEntity user = usersList.get(0);

        System.out.println(">>createdDate="+ user.getCreatedDate() + ", modifiedDate=" + user.getModifiedDate());

        // then
        assertThat(user.getCreatedDate()).isAfter(now);
        assertThat(user.getModifiedDate()).isAfter(now);

    }


}