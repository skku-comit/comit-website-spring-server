package com.example.comitserver.controller;

import com.example.comitserver.dto.CustomUserDetails;
import com.example.comitserver.dto.StudyRequestDTO;
import com.example.comitserver.dto.StudyResponseDTO;
import com.example.comitserver.entity.StudyEntity;
import com.example.comitserver.service.StudyService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class StudyController {

    private final StudyService studyService;
    private final ModelMapper modelMapper;

    @Autowired
    public StudyController(StudyService studyService, ModelMapper modelMapper) {
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

    @PostMapping("/studies")
    public ResponseEntity<StudyResponseDTO> postStudy(@RequestBody StudyRequestDTO studyRequestDTO, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        StudyEntity newStudy = studyService.createStudy(studyRequestDTO, customUserDetails);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newStudy.getId())
                .toUri();

        return ResponseEntity.created(location).body(modelMapper.map(newStudy, StudyResponseDTO.class));
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
        StudyResponseDTO studyResponseDTO = modelMapper.map(deletedStudy, StudyResponseDTO.class);

        studyService.deleteStudy(id);

        return ResponseEntity.ok(studyResponseDTO);
    }

}
