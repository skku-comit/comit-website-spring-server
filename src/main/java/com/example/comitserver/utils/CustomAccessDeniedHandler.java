package com.example.comitserver.utils;

import com.example.comitserver.dto.ServerResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    // spring security에서 기본으로 처리하는 403 에러를 통일되게 표현
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        ResponseUtil.createFilterErrorResponse(response, HttpStatus.FORBIDDEN, "Forbidden", "You do not have permission to access this resource.");
    }

}