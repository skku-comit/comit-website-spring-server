package com.example.comitserver.utils;

import com.example.comitserver.dto.ServerResponseDTO;
import com.example.comitserver.exception.DuplicateResourceException;
import com.example.comitserver.exception.ResourceNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // ResourceNotFoundException 예외 처리
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ServerResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return ResponseUtil.createErrorResponse(HttpStatus.NOT_FOUND, "Resource Not Found", ex.getMessage());
    }

    // UsernameNotFoundException 처리
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ServerResponseDTO> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        return ResponseUtil.createErrorResponse(HttpStatus.NOT_FOUND, "User Not Found", ex.getMessage());
    }

    // Validation 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ServerResponseDTO> handleValidationException(MethodArgumentNotValidException ex) {
        String validationErrors = ex.getBindingResult().toString();
        return ResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST, "Validation Error", validationErrors);
    }

    // Spring Security 인증 관련 예외 처리
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ServerResponseDTO> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        return ResponseUtil.createErrorResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", "Authentication failed.123");
    }

    // JWT 토큰 만료 및 기타 JWT 예외 처리
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ServerResponseDTO> handleExpiredJwtException(ExpiredJwtException ex, WebRequest request) {
        return ResponseUtil.createErrorResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", "The access token has expired.123");
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ServerResponseDTO> handleJwtException(JwtException ex, WebRequest request) {
        return ResponseUtil.createErrorResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", "Invalid JWT token.123");
    }

    // user detail null 토큰 없을 때 예외 처리
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ServerResponseDTO> handleNullPointerException(NullPointerException ex, WebRequest request) {
        return ResponseUtil.createErrorResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", "Authentication not provided.");
    }

    // update 할때 중복 예외 처리
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ServerResponseDTO> handleDuplicateResourceException(DuplicateResourceException ex) {
        return ResponseUtil.createErrorResponse(
                HttpStatus.CONFLICT,
                "Duplicate Resource",
                ex.getMessage()
        );
    }

    // IllegalArgumentException 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ServerResponseDTO> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return ResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage());
    }

    // 기타 모든 예외 처리 (Global Exception Handling)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ServerResponseDTO> handleGlobalException(Exception ex, WebRequest request) {
        return ResponseUtil.createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage());
    }



}