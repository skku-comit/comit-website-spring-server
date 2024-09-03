package com.example.comitserver.utils;

import com.example.comitserver.dto.ServerResponseDTO;
import com.example.comitserver.exception.DuplicateResourceException;
import com.example.comitserver.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

    // 다양한 null pointer exception 처리
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ServerResponseDTO> handleNullPointerException(NullPointerException ex, WebRequest request) {
        String endpoint = ResponseUtil.extractEndpoint(request);

        // 기본 에러 타입과 메시지 설정
        String errorType = endpoint + "/null_pointer_exception";
        String errorMessage = "A null pointer exception occurred.";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        // 특정 시나리오에서의 커스터마이즈
        if (endpoint.contains("/auth") || endpoint.contains("/login") || endpoint.contains("/token")) {
            errorType = endpoint + "/unauthorized";
            errorMessage = "Authentication not provided.";
            status = HttpStatus.UNAUTHORIZED;
        } else if (endpoint.contains("/users")) {
            errorType = endpoint + "/user_error";
            errorMessage = "A null pointer exception occurred while processing user data.";
            status = HttpStatus.BAD_REQUEST;
        }

        return ResponseUtil.createErrorResponse(status, errorType, errorMessage);
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

    // JSON 파싱 오류 등 메시지 읽기 오류 처리
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ServerResponseDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
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