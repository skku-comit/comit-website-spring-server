package com.example.comitserver.controller;

import com.example.comitserver.dto.JoinDTO;
import com.example.comitserver.dto.ServerResponseDTO;
import com.example.comitserver.dto.UserDTO;
import com.example.comitserver.entity.UserEntity;
import com.example.comitserver.exception.DuplicateResourceException;
import com.example.comitserver.service.JoinService;
import com.example.comitserver.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class JoinController {
    private final JoinService joinService;
    private final ModelMapper modelMapper;

    public JoinController(JoinService joinService, ModelMapper modelMapper) {
        this.joinService = joinService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinProcess(@RequestBody @Valid JoinDTO joinDTO, HttpServletRequest request) {
        UserEntity createdUser = joinService.joinProcess(joinDTO);
        UserDTO userDTO = modelMapper.map(createdUser, UserDTO.class);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/users/{id}")
                .buildAndExpand(createdUser.getId())
                .toUri();

        return ResponseUtil.createSuccessResponse(userDTO, HttpStatus.CREATED, location);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<?> handleDuplicateResourceException(DuplicateResourceException ex, HttpServletRequest request) {
        String endpoint = extractEndpoint(request.getRequestURI());
        return ResponseUtil.createErrorResponse(
                HttpStatus.CONFLICT,
                endpoint + "/duplicate_resource",
                ex.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ServerResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String errorMessage = ex.getBindingResult().getAllErrors().stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Validation error");

        String endpoint = extractEndpoint(request.getRequestURI());

        return ResponseUtil.createErrorResponse(
                HttpStatus.BAD_REQUEST,
                endpoint + "/validation_failed",
                errorMessage
        );
    }

    // WebRequest에서 엔드포인트 정보를 추출하는 메서드
    private String extractEndpoint(String uri) {
        // "api/"로 시작하면 이를 제거
        if (uri.startsWith("/api/")) {
            uri = uri.substring(5); // "api/"를 제거
        }

        // Path Variable 부분 제거
        uri = uri.replaceAll("/\\d+", "")  // 숫자로 된 ID 제거
                .replaceAll("/\\{[^/]+\\}", ""); // {}로 감싸진 Path Variable 제거

        return uri;
    }
}