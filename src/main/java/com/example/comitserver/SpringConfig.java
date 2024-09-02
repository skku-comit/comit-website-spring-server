package com.example.comitserver;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Entity와 DTO의 attribute가 아예 똑같은 게 아니라면 이런 식으로 설정할 수 있다.
//        modelMapper.addMappings(new PropertyMap<StudyEntity, StudyResponseDTO>() {
//            @Override
//            protected void configure() {
//                map(source.getMentor().getUsername(), destination.getMentorName());
//                map(source.getMentor().getId(), destination.getMentorId());
//            }
//        });
        return modelMapper;
    }
}
