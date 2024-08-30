package com.example.comitserver.controller;

import com.example.comitserver.dto.StudyRequestDTO;
import com.example.comitserver.dto.StudyResponseDTO;
import com.example.comitserver.entity.StudyEntity;
import com.example.comitserver.service.StudyService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class StudyAdminController {

    private final StudyService studyService;
    private final ModelMapper modelMapper;

    public StudyAdminController(StudyService studyService, ModelMapper modelMapper) {
        this.studyService = studyService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/studies")
    public ResponseEntity<List<StudyResponseDTO>> getStudies() {
        List<StudyEntity> allStudies = studyService.showAllStudies();
        List<StudyResponseDTO> allStudiesDTO = allStudies.stream().map(entity -> modelMapper.map(entity, StudyResponseDTO.class)).collect(Collectors.toList());

        return ResponseEntity.ok(allStudiesDTO);
    }

    @GetMapping("/studies/{id}")
    public ResponseEntity<StudyResponseDTO> getStudyById(@PathVariable Long id) {
        StudyEntity study = studyService.showStudy(id);

        return ResponseEntity.ok(modelMapper.map(study, StudyResponseDTO.class));
    }

    @PutMapping("/studies/{id}")
    public ResponseEntity<StudyResponseDTO> putStudy(@PathVariable Long id, @RequestBody StudyRequestDTO studyRequestDTO) {
        StudyEntity updatedStudy = studyService.updateStudy(id, studyRequestDTO);

        if (updatedStudy == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(modelMapper.map(updatedStudy, StudyResponseDTO.class));
    }

    @DeleteMapping("/studies/{id}")
    public ResponseEntity<StudyResponseDTO> deleteStudy(@PathVariable Long id) {
        StudyEntity deletedStudy = studyService.showStudy(id);

        studyService.deleteStudy(id);

        return ResponseEntity.ok(modelMapper.map(deletedStudy, StudyResponseDTO.class));
    }

}
