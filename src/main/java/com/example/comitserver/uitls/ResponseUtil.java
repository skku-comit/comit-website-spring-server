package com.example.comitserver.uitls;

import com.example.comitserver.dto.ServerErrorDTO;
import com.example.comitserver.dto.ServerResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {

    // Create error response in controller
    public static ResponseEntity<ServerResponseDTO> createErrorResponse(HttpStatus status, String errorType, String detail) {
        ServerErrorDTO errorDTO = new ServerErrorDTO(errorType + "/" + status.toString().split(" ")[1], detail);
        ServerResponseDTO responseDTO = new ServerResponseDTO(errorDTO, null);
        return new ResponseEntity<>(responseDTO, status);
    }

    // Create success response in controller
    public static ResponseEntity<ServerResponseDTO> createSuccessResponse(Object data, HttpStatus status) {
        ServerResponseDTO responseDTO = new ServerResponseDTO(null, data);
        return new ResponseEntity<>(responseDTO, status);
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
                errorType + "/" + status.toString().split(" ")[1],
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
}