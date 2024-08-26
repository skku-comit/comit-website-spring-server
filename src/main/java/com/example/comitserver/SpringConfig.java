package com.example.comitserver;

import com.example.comitserver.repository.StudyRepository;
import com.example.comitserver.repository.UserRepository;
import com.example.comitserver.service.StudyService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    private final StudyRepository studyRepository;

    @Autowired
    public SpringConfig(StudyRepository studyRepository) {this.studyRepository = studyRepository;}

    @Bean
    public StudyService studyService() {
        return new StudyService(studyRepository);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
