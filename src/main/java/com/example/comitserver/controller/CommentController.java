package com.example.comitserver.controller;

import com.example.comitserver.config.auth.CustomUserDetails;
import com.example.comitserver.dto.CommentDTO;
import com.example.comitserver.dto.ProgressDTO;
import com.example.comitserver.dto.ServerResponseDTO;
import com.example.comitserver.entity.Comment;
import com.example.comitserver.repository.CommentRepository;
import com.example.comitserver.service.CommentService;
import com.example.comitserver.service.StudyUserService;
import com.example.comitserver.utils.ResponseUtil;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/studies/{studyId}")
public class CommentController {
    private final CommentService commentService;
    private final StudyUserService studyUserService;
    private final ModelMapper modelMapper;
    private final CommentRepository commentRepository;

    public CommentController(CommentService commentService, StudyUserService studyUserService, ModelMapper modelMapper, CommentRepository commentRepository) {
        this.commentService = commentService;
        this.studyUserService = studyUserService;
        this.modelMapper = modelMapper;
        this.commentRepository = commentRepository;
    }

    @GetMapping("/comments")
    public ResponseEntity<ServerResponseDTO> getAllComments(@PathVariable Long studyId) {
        List<Comment> allComment = commentService.showAllComments(studyId);
        List<CommentDTO> allCommentDTO = allComment.stream().map(entity -> modelMapper.map(entity, CommentDTO.class)).toList();

        return ResponseUtil.createSuccessResponse(allCommentDTO, HttpStatus.OK);
    }

    @PostMapping("/comments")
    public ResponseEntity<ServerResponseDTO> postProgress(@RequestBody CommentDTO commentDTO, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (!studyUserService.isMember(commentDTO.getStudyId(), customUserDetails)) {
            return ResponseUtil.createErrorResponse(HttpStatus.FORBIDDEN, "Progress/PermissionDenied", "the user does not have permission to create comment");
        }

        Comment newComment = commentService.createComment(commentDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{commentId}")
                .buildAndExpand(newComment.getId())
                .toUri();

        return ResponseUtil.createSuccessResponse(modelMapper.map(newComment, CommentDTO.class), HttpStatus.CREATED, location);
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<ServerResponseDTO> patchComment(@PathVariable Long commentId, @RequestBody CommentDTO commentDTO, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (commentRepository.findById(commentId).isEmpty()) {
            return ResponseUtil.createErrorResponse(HttpStatus.NOT_FOUND, "Comment/CannotFindId", "Comment with the given ID was not found");
        }

        if (commentService.isWriter(commentId, customUserDetails)) {
            Comment existingComment = commentService.showComment(commentId);

            commentService.updateComment(commentId, commentDTO);
            return ResponseUtil.createSuccessResponse(modelMapper.map(existingComment, ProgressDTO.class), HttpStatus.OK);
        }
        else return ResponseUtil.createErrorResponse(HttpStatus.FORBIDDEN, "Comment/PermissionDenied", "the user does not have permission to update this comment");
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ServerResponseDTO> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (commentRepository.findById(commentId).isEmpty()) {
            return ResponseUtil.createErrorResponse(HttpStatus.NOT_FOUND, "Comment/CannotFindId", "Comment with the given ID was not found");
        }

        if(commentService.isWriter(commentId, customUserDetails)) {
            Comment deletedComment = commentService.showComment(commentId);
            CommentDTO commentDTO = modelMapper.map(deletedComment, CommentDTO.class);

            commentService.deleteComment(commentId);

            return ResponseUtil.createSuccessResponse(commentDTO, HttpStatus.OK);
        }
        else return ResponseUtil.createErrorResponse(HttpStatus.FORBIDDEN, "Comment/PermissionDenied", "the user does not have permission to delete this comment");
    }
}
