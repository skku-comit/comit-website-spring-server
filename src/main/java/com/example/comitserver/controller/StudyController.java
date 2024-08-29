package com.example.comitserver.controller;

import com.example.comitserver.dto.StudyDTO;
import com.example.comitserver.entity.StudyEntity;
import com.example.comitserver.service.StudyService;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<StudyDTO>> getStudies() {
        List<StudyEntity> allStudies = studyService.showAllStudies();
        List<StudyDTO> allStudiesDTO = allStudies.stream().map(entity -> modelMapper.map(entity, StudyDTO.class)).collect(Collectors.toList());

        return ResponseEntity.ok(allStudiesDTO);
    }

    @GetMapping("/studies/{id}")
    public ResponseEntity<StudyDTO> getStudyById(@PathVariable Long id) {
        StudyEntity study = studyService.showStudy(id);

        return ResponseEntity.ok(modelMapper.map(study, StudyDTO.class));
    }

    @PostMapping("/studies")
    public ResponseEntity<StudyDTO> postStudy(@RequestBody StudyDTO studyDTO) {
        StudyEntity newStudy = studyService.createStudy(studyDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newStudy.getId())
                .toUri();

        return ResponseEntity.created(location).body(modelMapper.map(newStudy, StudyDTO.class));
    }

    @PutMapping("/studies/{id}")
    public ResponseEntity<StudyDTO> putStudy(@PathVariable Long id, @RequestBody StudyDTO studyDTO) {
        StudyEntity updatedStudy = studyService.updateStudy(id, studyDTO);

        if (updatedStudy == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(modelMapper.map(updatedStudy, StudyDTO.class));
    }

    @DeleteMapping("/studies/{id}")
    public ResponseEntity<StudyDTO> deleteStudy(@PathVariable Long id) {
        StudyEntity deletedStudy = studyService.showStudy(id);

        studyService.deleteStudy(id);

        return ResponseEntity.ok(modelMapper.map(deletedStudy, StudyDTO.class));
    }

}
