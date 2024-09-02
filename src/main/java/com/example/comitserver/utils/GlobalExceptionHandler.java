package com.example.comitserver.utils;

import com.example.comitserver.dto.ServerResponseDTO;
import com.example.comitserver.exception.DuplicateResourceException;
import com.example.comitserver.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.naming.AuthenticationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ServerResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        String errorType = ResponseUtil.extractEndpoint(request) + "/resource_not_found";
        return ResponseUtil.createErrorResponse(HttpStatus.NOT_FOUND, errorType, ex.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ServerResponseDTO> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        String errorType = ResponseUtil.extractEndpoint(request) + "/user_not_found";
        return ResponseUtil.createErrorResponse(HttpStatus.NOT_FOUND, errorType, ex.getMessage());
    }

    // 필드 Validation 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ServerResponseDTO> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        String validationErrors = ex.getBindingResult().toString();
        String errorType = ResponseUtil.extractEndpoint(request) + "/validation_error";
        return ResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST, errorType, validationErrors);
    }

    // Spring Security 인증 관련 예외 처리
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ServerResponseDTO> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        String errorType = ResponseUtil.extractEndpoint(request) + "/unauthorized";
        return ResponseUtil.createErrorResponse(HttpStatus.UNAUTHORIZED, errorType, "Authentication failed.");
    }

//    // JWT 토큰 만료 및 기타 JWT 예외 처리
//    @ExceptionHandler(ExpiredJwtException.class)
//    public ResponseEntity<ServerResponseDTO> handleExpiredJwtException(ExpiredJwtException ex, WebRequest request) {
//        String errorType = ResponseUtil.extractEndpoint(request) + "/token_expired";
//        return ResponseUtil.createErrorResponse(HttpStatus.UNAUTHORIZED, errorType, "The access token has expired.");
//    }
//
//    @ExceptionHandler(JwtException.class)
//    public ResponseEntity<ServerResponseDTO> handleJwtException(JwtException ex, WebRequest request) {
//        String errorType = ResponseUtil.extractEndpoint(request) + "/invalid_token";
//        return ResponseUtil.createErrorResponse(HttpStatus.UNAUTHORIZED, errorType, "Invalid JWT token.");
//    } -> jwtfilter에서 처리되는것으로 확인됨

    // user detail null 토큰 없을 때 예외 처리
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ServerResponseDTO> handleNullPointerException(NullPointerException ex, WebRequest request) {
        String errorType = ResponseUtil.extractEndpoint(request) + "/unauthorized";
        return ResponseUtil.createErrorResponse(HttpStatus.UNAUTHORIZED, errorType, "Authentication not provided.");
    }

    // update 할때 중복 예외 처리
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ServerResponseDTO> handleDuplicateResourceException(DuplicateResourceException ex, WebRequest request) {
        String errorType = ResponseUtil.extractEndpoint(request) + "/duplicate_resource";
        return ResponseUtil.createErrorResponse(HttpStatus.CONFLICT, errorType, ex.getMessage());
    }

    // IllegalArgumentException 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ServerResponseDTO> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        String errorType = ResponseUtil.extractEndpoint(request) + "/bad_request";
        return ResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST, errorType, ex.getMessage());
    }

    // 기타 모든 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ServerResponseDTO> handleGlobalException(Exception ex, WebRequest request) {
        String errorType = ResponseUtil.extractEndpoint(request) + "/internal_server_error";
        return ResponseUtil.createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, errorType, ex.getMessage());
    }
}