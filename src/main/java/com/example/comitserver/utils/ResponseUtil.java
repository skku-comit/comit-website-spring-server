package com.example.comitserver.utils;

import com.example.comitserver.dto.ServerErrorDTO;
import com.example.comitserver.dto.ServerResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;
import java.io.PrintWriter;

import java.net.URI;

import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {

    // Create error response in controller
    public static ResponseEntity<ServerResponseDTO> createErrorResponse(HttpStatus status, String errorType, String detail) {
        ServerErrorDTO errorDTO = new ServerErrorDTO(errorType, detail);
        ServerResponseDTO responseDTO = new ServerResponseDTO(errorDTO, null);
        return new ResponseEntity<>(responseDTO, status);
    }

    // Create success response in controller
    public static ResponseEntity<ServerResponseDTO> createSuccessResponse(Object data, HttpStatus status) {
        ServerResponseDTO responseDTO = new ServerResponseDTO(null, data);
        return new ResponseEntity<>(responseDTO, status);
    }

    // Create success response in controller with Location header
    public static ResponseEntity<ServerResponseDTO> createSuccessResponse(Object data, HttpStatus status, URI location) {
        ServerResponseDTO responseDTO = new ServerResponseDTO(null, data);
        return ResponseEntity.status(status)
                .location(location)
                .body(responseDTO);
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Write error response in filter
    public static void writeErrorResponse(HttpServletResponse response, HttpStatus status, String errorType, String detail) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("data", null);
        responseBody.put("error", new ServerErrorDTO(
                errorType,
                detail
        ));

        try (PrintWriter out = response.getWriter()) {
            out.write(objectMapper.writeValueAsString(responseBody));
        }
    }

    // Write success response in filter
    public static void writeSuccessResponse(HttpServletResponse response, Object data, HttpStatus status) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("data", data);
        responseBody.put("error", null);

        try (PrintWriter out = response.getWriter()) {
            out.write(objectMapper.writeValueAsString(responseBody));
        }
    }

    // Write success response in filter with Location header
    public static void writeSuccessResponse(HttpServletResponse response, Object data, HttpStatus status, URI location) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("data", data);
        responseBody.put("error", null);

        // Add Location header
        response.setHeader("Location", location.toString());

        try (PrintWriter out = response.getWriter()) {
            out.write(objectMapper.writeValueAsString(responseBody));
        }
    }

    // URI에서 엔드포인트 정보를 추출하는 메서드
    public static String extractEndpoint(String uri) {
        // "api/"로 시작하면 이를 제거
        if (uri.startsWith("/api/")) {
            uri = uri.substring(5); // "api/"를 제거
        }

        // Path Variable 부분 제거
        uri = uri.replaceAll("/\\d+", "")  // 숫자로 된 ID 제거
                .replaceAll("/\\{[^/]+\\}", ""); // {}로 감싸진 Path Variable 제거

        return uri;
    }

    // WebRequest에서 엔드포인트 정보를 추출하는 메서드
    public static String extractEndpoint(WebRequest request) {
        String uri = request.getDescription(false)
                .replace("uri=", "")
                .split("\\?")[0]; // 쿼리 파라미터 제거

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