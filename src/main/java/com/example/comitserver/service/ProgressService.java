package com.example.comitserver.service;

import com.example.comitserver.dto.ProgressDTO;
import com.example.comitserver.entity.Progress;
import com.example.comitserver.entity.Study;
import com.example.comitserver.repository.ProgressRepository;
import com.example.comitserver.repository.StudyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ProgressService {

    private final ProgressRepository progressRepository;
    private final StudyRepository studyRepository;

    public ProgressService(ProgressRepository progressRepository, StudyRepository studyRepository) {
        this.progressRepository = progressRepository;
        this.studyRepository = studyRepository;
    }

    public List<Progress> showAllProgress(Long studyId) {
        return progressRepository.findByStudyId(studyId);
    }

    public Progress showProgress(Long id) {
        return progressRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Progress not found with id: " + id));
    }

    public Progress createProgress(ProgressDTO progressDTO) {
        Study study = studyRepository.findById(progressDTO.getStudyId())
                .orElseThrow(() -> new NoSuchElementException("Semester not found with id: " + progressDTO.getStudyId()));

        Progress progress = Progress.builder()
                .study(study)
                .week(progressDTO.getWeek())
                .title(progressDTO.getTitle())
                .content(progressDTO.getContent())
                .status(progressDTO.getStatus())
                .build();
        progressRepository.save(progress);

        return progress;
    }

    public Progress updateProgress(Long id, ProgressDTO progressDTO) {
        Progress updatingProgress = showProgress(id);

        if (progressDTO.getWeek() != null) {
            updatingProgress.setWeek(progressDTO.getWeek());
        }
        if (progressDTO.getTitle() != null) {
            updatingProgress.setTitle(progressDTO.getTitle());
        }
        if (progressDTO.getContent() != null) {
            updatingProgress.setContent(progressDTO.getContent());
        }
        if (progressDTO.getStatus() != null) {
            updatingProgress.setStatus(progressDTO.getStatus());
        }
        progressRepository.save(updatingProgress);

        return updatingProgress;
    }

    public void deleteProgress(Long id) {
        progressRepository.deleteById(id);
    }

    public Long showStudyIdByProgressId(Long id) {
        Progress progress = progressRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Progress not found with id: " + id));
        return progress.getStudy().getId();
    }
}
