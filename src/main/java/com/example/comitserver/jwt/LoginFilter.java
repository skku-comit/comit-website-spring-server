package com.example.comitserver.jwt;

import com.example.comitserver.dto.*;
import com.example.comitserver.entity.RefreshEntity;
import com.example.comitserver.repository.RefreshRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, RefreshRepository refreshRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
        setFilterProcessesUrl("/api/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginDTO loginDTO;

        try {
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            loginDTO = objectMapper.readValue(messageBody, LoginDTO.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse login request", e);
        }

        String email = loginDTO.getEmail();
        String password = loginDTO.getPassword();

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
        return authenticationManager.authenticate(authToken);
    }

    private void writeResponse(HttpServletResponse response, ServerResponseDTO serverResponseDTO, HttpStatus status) throws IOException {
        response.setStatus(status.value()); // Set the HTTP status code
        objectMapper.writeValue(response.getWriter(), serverResponseDTO); // Write the JSON response body
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();
        String username = userDetails.getUsername();

        String accessToken = jwtUtil.createJwt("access", userId, 1800000L);
        String refreshToken = jwtUtil.createJwt("refresh", userId, 1209600000L);

        addRefreshEntity(userId, refreshToken);

        response.setHeader("access", accessToken);
        response.addCookie(JWTUtil.createCookie("refresh", refreshToken));
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> user = new HashMap<>();
        user.put("id", userId);
        user.put("username", username);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("data", user);
        responseBody.put("error", null);

        objectMapper.writeValue(response.getWriter(), responseBody);
        response.setStatus(HttpStatus.OK.value());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> responseBody = new HashMap<>();

        if (failed instanceof BadCredentialsException) {
            responseBody.put("data", null);
            responseBody.put("error", new ServerErrorDTO(
                    "401 Unauthorized",
                    "Authentication Failed",
                    "Invalid email or password."
            ));
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            responseBody.put("data", null);
            responseBody.put("error", new ServerErrorDTO(
                    "400 Bad Request",
                    "Authentication Failed",
                    "An unknown error occurred. Please check your request and try again later."
            ));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        objectMapper.writeValue(response.getWriter(), responseBody);
    }

    private void addRefreshEntity(Long userId, String refresh) {
        Date date = new Date(System.currentTimeMillis() + 1209600000L);

        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setUserId(userId);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }
}
