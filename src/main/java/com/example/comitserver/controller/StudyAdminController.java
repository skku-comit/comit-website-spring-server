package com.example.comitserver.controller;

import com.example.comitserver.dto.ServerResponseDTO;
import com.example.comitserver.dto.StudyRequestDTO;
import com.example.comitserver.dto.StudyResponseDTO;
import com.example.comitserver.entity.StudyEntity;
import com.example.comitserver.repository.StudyRepository;
import com.example.comitserver.service.StudyService;
import com.example.comitserver.utils.ResponseUtil;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class StudyAdminController {

    private final StudyService studyService;
    private final ModelMapper modelMapper;
    private final StudyRepository studyRepository;

    public StudyAdminController(StudyService studyService, ModelMapper modelMapper, StudyRepository studyRepository) {
        this.studyService = studyService;
        this.modelMapper = modelMapper;
        this.studyRepository = studyRepository;
    }

    @GetMapping("/studies")
    public ResponseEntity<ServerResponseDTO> getStudies() {
        List<StudyEntity> allStudies = studyService.showAllStudies();
        List<StudyResponseDTO> allStudiesDTO = allStudies.stream().map(entity -> modelMapper.map(entity, StudyResponseDTO.class)).collect(Collectors.toList());

        return ResponseUtil.createSuccessResponse(allStudiesDTO, HttpStatus.OK);
        //return ResponseEntity.ok(allStudiesDTO);
    }

    @GetMapping("/studies/{id}")
    public ResponseEntity<ServerResponseDTO> getStudyById(@PathVariable Long id) {
        if (studyRepository.findById(id).isEmpty()) return ResponseUtil.createErrorResponse(HttpStatus.NOT_FOUND, "Study/CannotFindId", "study with that id not found");

        StudyEntity study = studyService.showStudy(id);

        return ResponseUtil.createSuccessResponse(study, HttpStatus.OK);
        //return ResponseEntity.ok(modelMapper.map(study, StudyResponseDTO.class));
    }

    @PutMapping("/studies/{id}")
    public ResponseEntity<ServerResponseDTO> putStudy(@PathVariable Long id, @RequestBody StudyRequestDTO studyRequestDTO) {
        if (studyRepository.findById(id).isEmpty()) return ResponseUtil.createErrorResponse(HttpStatus.NOT_FOUND, "Study/CannotFindId", "study with that id not found");


        StudyEntity updatedStudy = studyService.updateStudy(id, studyRequestDTO);

        if (updatedStudy == null) {
            return ResponseUtil.createErrorResponse(HttpStatus.NOT_FOUND, "Study/CannotFindId", "the result of updating study is null");
            //return ResponseEntity.notFound().build();
        }

        return ResponseUtil.createSuccessResponse(modelMapper.map(updatedStudy, StudyResponseDTO.class), HttpStatus.OK);
        //return ResponseEntity.ok(modelMapper.map(updatedStudy, StudyResponseDTO.class));

    }

    @PatchMapping("/studies/{id}")
    public ResponseEntity<ServerResponseDTO> patchIsRecruiting(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        if (studyRepository.findById(id).isEmpty()) return ResponseUtil.createErrorResponse(HttpStatus.NOT_FOUND, "Study/CannotFindId", "study with that id not found");

        StudyEntity study = studyService.showStudy(id);
        study.setIsRecruiting(body.get("isRecruiting"));
        studyRepository.save(study);

        Map<String, Boolean> isRecruiting = new HashMap<>();
        isRecruiting.put("isRecruiting", study.getIsRecruiting());

        return ResponseUtil.createSuccessResponse(isRecruiting, HttpStatus.OK);

    }

    @DeleteMapping("/studies/{id}")
    public ResponseEntity<ServerResponseDTO> deleteStudy(@PathVariable Long id) {
        if (studyRepository.findById(id).isEmpty()) return ResponseUtil.createErrorResponse(HttpStatus.NOT_FOUND, "Study/CannotFindId", "study with that id not found");


        StudyEntity deletedStudy = studyService.showStudy(id);
        StudyResponseDTO studyResponseDTO = modelMapper.map(deletedStudy, StudyResponseDTO.class);

        studyService.deleteStudy(id);

        return ResponseUtil.createSuccessResponse(studyResponseDTO, HttpStatus.OK);
        //return ResponseEntity.ok(studyResponseDTO);

    }

}
