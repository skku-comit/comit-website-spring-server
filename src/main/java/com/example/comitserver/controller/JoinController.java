package com.example.comitserver.controller;

import com.example.comitserver.dto.JoinDTO;
import com.example.comitserver.dto.ServerErrorDTO;
import com.example.comitserver.dto.ServerResponseDTO;
import com.example.comitserver.entity.UserEntity;
import com.example.comitserver.exception.DuplicateResourceException;
import com.example.comitserver.service.JoinService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class JoinController {
    private final JoinService joinService;

    public JoinController(JoinService joinService) {
        this.joinService = joinService;
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinProcess(@RequestBody JoinDTO joinDTO) {
        UserEntity createdUser = joinService.joinProcess(joinDTO);

        return new ResponseEntity<>(
                new ServerResponseDTO(null,
                        createdUser
                ), HttpStatus.OK
        );
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<?> handleDuplicateResourceException(DuplicateResourceException ex) {
        return new ResponseEntity<>(
                new ServerResponseDTO(
                        new ServerErrorDTO(
                                "409 Conflict",
                                "Duplicate Resource",
                                ex.getMessage()
                        ),
                        null
                ),
                HttpStatus.CONFLICT
        );
    }

    //TODO: validation 추가
}