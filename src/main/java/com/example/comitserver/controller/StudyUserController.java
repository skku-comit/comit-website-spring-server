package com.example.comitserver.controller;

import com.example.comitserver.config.auth.CustomUserDetails;
import com.example.comitserver.dto.ServerResponseDTO;
import com.example.comitserver.dto.StudyResponseDTO;
import com.example.comitserver.dto.StudyUserDTO;
import com.example.comitserver.entity.StudyUser;
import com.example.comitserver.repository.StudyRepository;
import com.example.comitserver.repository.StudyUserRepository;
import com.example.comitserver.service.StudyService;
import com.example.comitserver.service.StudyUserService;
import com.example.comitserver.utils.ResponseUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/studies/{studyId}")
public class StudyUserController {
    private final StudyUserService studyUserService;
    private final StudyService studyService;
    private final ModelMapper modelMapper;
    private final StudyRepository studyRepository;
    private final StudyUserRepository studyUserRepository;

    @Autowired
    public StudyUserController(StudyUserService studyUserService, StudyService studyService, ModelMapper modelMapper, StudyRepository studyRepository, StudyUserRepository studyUserRepository) {
        this.studyUserService = studyUserService;
        this.studyService = studyService;
        this.modelMapper = modelMapper;
        this.studyRepository = studyRepository;
        this.studyUserRepository = studyUserRepository;
    }

    @GetMapping("/applications")
    public ResponseEntity<ServerResponseDTO> getStudyUser(@PathVariable Long studyId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (!studyService.isLeader(studyId, customUserDetails)) {
            return ResponseUtil.createErrorResponse(HttpStatus.FORBIDDEN, "Study/PermissionDenied", "the user does not have permission to check the applicants");
        }
        List<StudyUser> studyUsers = studyUserService.showStudyUsers(studyId);
        List<StudyUserDTO> studyUserDTOS = studyUsers.stream().map(entity -> modelMapper.map(entity, StudyUserDTO.class))
                .collect(Collectors.toList());

        return ResponseUtil.createSuccessResponse(studyUserDTOS, HttpStatus.OK);
    }

    @PostMapping("/applications")
    public ResponseEntity<ServerResponseDTO> postStudyUser(@PathVariable Long studyId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (studyRepository.findById(studyId).isEmpty()) return ResponseUtil.createErrorResponse(HttpStatus.NOT_FOUND, "Study/CannotFindId", "Study with the given ID was not found");

        if (studyService.isLeader(studyId, customUserDetails)) {
            return ResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST, "Study/LeaderCannotApply", "The leader of the study cannot apply.");
        }
        else if(studyUserService.isAppliedAlready(studyId, customUserDetails)) {
            return ResponseUtil.createErrorResponse(HttpStatus.CONFLICT, "Study/AlreadyApplied", "You have already applied or are a member of this study.");
        }

        StudyUser application = studyUserService.applyForStudy(studyId, customUserDetails);

        return ResponseUtil.createSuccessResponse(modelMapper.map(application, StudyUserDTO.class), HttpStatus.CREATED);
    }

    @PatchMapping("/applications/{userId}")
    public ResponseEntity<ServerResponseDTO> patchStudyUser(@PathVariable Long studyId, @PathVariable Long userId, @RequestBody StudyUserDTO studyUserDTO, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (studyRepository.findById(studyId).isEmpty()) return ResponseUtil.createErrorResponse(HttpStatus.NOT_FOUND, "Study/CannotFindId", "Study with the given ID was not found");

        if (!studyService.isLeader(studyId, customUserDetails)) {
            return ResponseUtil.createErrorResponse(HttpStatus.FORBIDDEN, "Study/PermissionDenied", "the user does not have permission to change the applicants' position");
        }

        if (studyUserDTO.getUserId().equals(customUserDetails.getId())) {
            return ResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST, "Study/CannotChangePositionOfLeader", "The leader position cannot be changed.");
        }

        List<StudyUser> studyUsers = studyUserRepository.findByStudyId(studyId);
        for (StudyUser studyUser : studyUsers) {
            if (studyUser.getUser().getId().equals(userId)) {
                studyUserService.changeMemberPosition(studyUser, studyUserDTO);
                return ResponseUtil.createSuccessResponse(modelMapper.map(studyUser, StudyUserDTO.class), HttpStatus.OK);
            }
        }
        return ResponseUtil.createErrorResponse(HttpStatus.NOT_FOUND, "Study/CannotFindUserId", "User with the given ID was not found in this Study.");
    }
}
