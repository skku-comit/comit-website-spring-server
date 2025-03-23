package com.example.comitserver.service;

import com.example.comitserver.config.auth.CustomUserDetails;
import com.example.comitserver.dto.CommentDTO;
import com.example.comitserver.dto.ProgressDTO;
import com.example.comitserver.entity.Comment;
import com.example.comitserver.entity.Progress;
import com.example.comitserver.entity.Study;
import com.example.comitserver.entity.User;
import com.example.comitserver.repository.CommentRepository;
import com.example.comitserver.repository.StudyRepository;
import com.example.comitserver.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final StudyRepository studyRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, StudyRepository studyRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.studyRepository = studyRepository;
        this.userRepository = userRepository;
    }

    public List<Comment> showAllComments(Long studyId) {
        return commentRepository.findByStudyId(studyId);
    }

    public Comment showComment(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Comment not found with id: " + id));
    }

    public Comment createComment(CommentDTO commentDTO) {
        Study study = studyRepository.findById(commentDTO.getStudyId())
                .orElseThrow(() -> new NoSuchElementException("Study not found with id: " + commentDTO.getStudyId()));

        User user = userRepository.findById(commentDTO.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + commentDTO.getUserId()));

        Comment newComment = Comment.builder()
                .study(study)
                .user(user)
                .editedDateTime(stringToLocalDateTime(commentDTO.getEditedDateTime()))
                .text(commentDTO.getText())
                .build();
        commentRepository.save(newComment);

        return newComment;
    }

    public Comment updateComment(Long id, CommentDTO commentDTO) {
        Comment updatingComment = showComment(id);

        if (commentDTO.getEditedDateTime() != null) {
            updatingComment.setEditedDateTime(stringToLocalDateTime(commentDTO.getEditedDateTime()));
        }
        if (commentDTO.getText() != null) {
            updatingComment.setText(commentDTO.getText());
        }
        commentRepository.save(updatingComment);

        return updatingComment;
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    public Boolean isWriter(Long id, CustomUserDetails customUserDetails) {
        User writer = showComment(id).getUser();
        if (customUserDetails.getId().equals(writer.getId())) {
            return true;
        }
        return false;
    }

    public LocalDateTime stringToLocalDateTime(String dateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, dateTimeFormatter);
        return localDateTime;
    }
}
