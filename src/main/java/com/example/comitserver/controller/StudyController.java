package com.example.comitserver.controller;

import com.example.comitserver.config.auth.CustomUserDetails;
import com.example.comitserver.dto.ServerResponseDTO;
import com.example.comitserver.dto.StudyRequestDTO;
import com.example.comitserver.dto.StudyResponseDTO;
import com.example.comitserver.entity.StudyEntity;
import com.example.comitserver.repository.StudyRepository;
import com.example.comitserver.service.StudyService;
import com.example.comitserver.utils.ResponseUtil;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private final StudyRepository studyRepository;

    @Autowired
    public StudyController(StudyService studyService, ModelMapper modelMapper, StudyRepository studyRepository) {
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

    @PostMapping("/studies")
    public ResponseEntity<ServerResponseDTO> postStudy(@RequestBody StudyRequestDTO studyRequestDTO, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        StudyEntity newStudy = studyService.createStudy(studyRequestDTO, customUserDetails);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newStudy.getId())
                .toUri();

        return ResponseUtil.createSuccessResponse(modelMapper.map(newStudy, StudyResponseDTO.class), HttpStatus.CREATED, location);
        //return ResponseEntity.created(location).body(modelMapper.map(newStudy, StudyResponseDTO.class));
    }

    @PatchMapping("/studies/{id}")
    public ResponseEntity<ServerResponseDTO> patchStudy(@PathVariable Long id, @RequestBody StudyRequestDTO studyRequestDTO, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (studyRepository.findById(id).isEmpty()) return ResponseUtil.createErrorResponse(HttpStatus.NOT_FOUND, "Study/CannotFindId", "study with that id not found");

        if (studyService.identification(id, customUserDetails)) {
            StudyEntity existingStudy = studyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Study not found"));

            if (studyRequestDTO.getTitle() != null) {
                existingStudy.setTitle(studyRequestDTO.getTitle());
            }
            if (studyRequestDTO.getImageSrc() != null) {
                existingStudy.setImageSrc(studyRequestDTO.getImageSrc());
            }
            if (studyRequestDTO.getDayOfWeek() != null) {
                existingStudy.setDayOfWeek(studyRequestDTO.getDayOfWeek());
            }
            if (studyRequestDTO.getStartTime() != null) {
                existingStudy.setStartTime(studyRequestDTO.getStartTime());
            }
            if (studyRequestDTO.getEndTime() != null) {
                existingStudy.setEndTime(studyRequestDTO.getEndTime());
            }
            if (studyRequestDTO.getLevel() != null) {
                existingStudy.setLevel(studyRequestDTO.getLevel());
            }
            if (studyRequestDTO.getStacks() != null) {
                existingStudy.setStacks(studyRequestDTO.getStacks());
            }
            if (studyRequestDTO.getLocation() != null) {
                existingStudy.setLocation(studyRequestDTO.getLocation());
            }
            if (studyRequestDTO.getDescription() != null) {
                existingStudy.setDescription(studyRequestDTO.getDescription());
            }
            if (studyRequestDTO.getIsRecruiting() != null) {
                existingStudy.setIsRecruiting(studyRequestDTO.getIsRecruiting());
            }
            if (studyRequestDTO.getSemester() != null) {
                existingStudy.setSemester(studyRequestDTO.getSemester());
            }
            studyRepository.save(existingStudy);
            return ResponseUtil.createSuccessResponse(modelMapper.map(existingStudy, StudyResponseDTO.class), HttpStatus.OK);
        }
        else return ResponseUtil.createErrorResponse(HttpStatus.FORBIDDEN, "Study/PermissionDenied", "the user does not have permission to update this study");
    }

//    @DeleteMapping("/studies/{id}")
//    public ResponseEntity<ServerResponseDTO> deleteStudy(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
//        if (studyRepository.findById(id).isEmpty()) return ResponseUtil.createErrorResponse(HttpStatus.NOT_FOUND, "Study/CannotFindId", "study with that id not found");
//
//        if(studyService.identification(id, customUserDetails)) {
//            StudyEntity deletedStudy = studyService.showStudy(id);
//            StudyResponseDTO studyResponseDTO = modelMapper.map(deletedStudy, StudyResponseDTO.class);
//
//            studyService.deleteStudy(id);
//
//            return ResponseUtil.createSuccessResponse(studyResponseDTO, HttpStatus.OK);
//            //return ResponseEntity.ok(studyResponseDTO);
//        }
//        else return ResponseUtil.createErrorResponse(HttpStatus.FORBIDDEN, "Study/PermissionDenied", "the user does not have permission to delete this study");
//                //ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//    }

}
