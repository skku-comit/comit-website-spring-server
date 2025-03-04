package com.example.comitserver.config.jwt;

import com.example.comitserver.config.auth.CustomUserDetails;
import com.example.comitserver.dto.*;
import com.example.comitserver.entity.RefreshEntity;
import com.example.comitserver.entity.enumeration.Role;
import com.example.comitserver.repository.RefreshRepository;
import com.example.comitserver.utils.ResponseUtil;
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
        UserLoginDTO userLoginDTO;

        try {
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            userLoginDTO = objectMapper.readValue(messageBody, UserLoginDTO.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse login request", e);
        }

        String name = userLoginDTO.getName();
        String password = userLoginDTO.getPassword();

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(name, password);
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();
        String username = userDetails.getUsername();
        Role role = userDetails.getRole();

//        String accessToken = jwtUtil.createJwt("access", userId, 1800000L);
        String accessToken = jwtUtil.createJwt("access", userId, 18000000L);
        String refreshToken = jwtUtil.createJwt("refresh", userId, 1209600000L);

        addRefreshEntity(userId, refreshToken);

        response.setHeader("access", accessToken);
        response.addCookie(JWTUtil.createCookie("refresh", refreshToken));

        Map<String, Object> user = new HashMap<>();
        user.put("id", userId);
        user.put("username", username);
        user.put("role", role);

        ResponseUtil.createFilterSuccessResponse(response, user, HttpStatus.OK);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        String endpoint = ResponseUtil.extractEndpoint(request.getRequestURI());

        if (failed instanceof BadCredentialsException) {
            ResponseUtil.createFilterErrorResponse(
                    response,
                    HttpStatus.UNAUTHORIZED,
                    endpoint + "/invalid_credentials",
                    "Invalid email or password."
            );
        } else {
            ResponseUtil.createFilterErrorResponse(
                    response,
                    HttpStatus.BAD_REQUEST,
                    endpoint + "/authentication_failed",
                    "An unknown error occurred. Please check your request and try again later."
            );
        }
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