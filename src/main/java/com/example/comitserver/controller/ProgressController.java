package com.example.comitserver.controller;

import com.example.comitserver.config.auth.CustomUserDetails;
import com.example.comitserver.dto.ProgressDTO;
import com.example.comitserver.dto.ServerResponseDTO;
import com.example.comitserver.dto.StudyRequestDTO;
import com.example.comitserver.dto.StudyResponseDTO;
import com.example.comitserver.entity.Progress;
import com.example.comitserver.entity.Study;
import com.example.comitserver.repository.ProgressRepository;
import com.example.comitserver.service.ProgressService;
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
@RequestMapping("/api/studies/{studyId}")
public class ProgressController {
    ProgressService progressService;
    StudyService studyService;
    ModelMapper modelMapper;
    ProgressRepository progressRepository;

    @Autowired
    public ProgressController(ProgressService progressService, StudyService studyService, ModelMapper modelMapper, ProgressRepository progressRepository) {
        this.progressService = progressService;
        this.studyService = studyService;
        this.modelMapper = modelMapper;
        this.progressRepository = progressRepository;
    }

    @GetMapping("/progress")
    public ResponseEntity<ServerResponseDTO> getAllProgress(@PathVariable Long studyId) {
        List<Progress> allProgress = progressService.showAllProgress(studyId);
        List<ProgressDTO> allProgressDTO = allProgress.stream().map(entity -> modelMapper.map(entity, ProgressDTO.class)).toList();

        return ResponseUtil.createSuccessResponse(allProgressDTO, HttpStatus.OK);
    }

    @PostMapping("/progress")
    public ResponseEntity<ServerResponseDTO> postProgress(@RequestBody ProgressDTO progressDTO, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (!studyService.isLeader(progressDTO.getStudyId(), customUserDetails)) {
            return ResponseUtil.createErrorResponse(HttpStatus.FORBIDDEN, "Progress/PermissionDenied", "the user does not have permission to create progress");
        }

        Progress newProgress = progressService.createProgress(progressDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{progressId}")
                .buildAndExpand(newProgress.getId())
                .toUri();

        return ResponseUtil.createSuccessResponse(modelMapper.map(newProgress, ProgressDTO.class), HttpStatus.CREATED, location);
    }

    @PatchMapping("/progress/{progressId}")
    public ResponseEntity<ServerResponseDTO> patchProgress(@PathVariable Long id, @RequestBody ProgressDTO progressDTO, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (progressRepository.findById(id).isEmpty()) {
            return ResponseUtil.createErrorResponse(HttpStatus.NOT_FOUND, "Progress/CannotFindId", "Progress with the given ID was not found");
        }

        if (studyService.isLeader(progressService.showStudyIdByProgressId(id), customUserDetails)) {
            Progress existingProgress = progressRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Study not found"));

            progressService.updateProgress(id, progressDTO);
            return ResponseUtil.createSuccessResponse(modelMapper.map(existingProgress, ProgressDTO.class), HttpStatus.OK);
        }
        else return ResponseUtil.createErrorResponse(HttpStatus.FORBIDDEN, "Progress/PermissionDenied", "the user does not have permission to update this progress");
    }

    @DeleteMapping("/progress/{progressId}")
    public ResponseEntity<ServerResponseDTO> deleteProgress(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (progressRepository.findById(id).isEmpty()) {
            return ResponseUtil.createErrorResponse(HttpStatus.NOT_FOUND, "Progress/CannotFindId", "Progress with the given ID was not found");
        }

        if(studyService.isLeader(progressService.showStudyIdByProgressId(id), customUserDetails)) {
            Progress deletedProgress = progressService.showProgress(id);
            ProgressDTO progressDTO = modelMapper.map(deletedProgress, ProgressDTO.class);

            progressService.deleteProgress(id);

            return ResponseUtil.createSuccessResponse(progressDTO, HttpStatus.OK);
        }
        else return ResponseUtil.createErrorResponse(HttpStatus.FORBIDDEN, "Progress/PermissionDenied", "the user does not have permission to delete this progress");
    }
}
