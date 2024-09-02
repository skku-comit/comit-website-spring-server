package com.example.comitserver.jwt;

import com.example.comitserver.repository.RefreshRepository;
import com.example.comitserver.utils.ResponseUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class CustomLogoutFilter extends GenericFilterBean {
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public CustomLogoutFilter(JWTUtil jwtUtil, RefreshRepository refreshRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^/api/logout$")) {
            filterChain.doFilter(request, response);
            return;
        }
        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        //get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh")) {
                    refresh = cookie.getValue();
                    break;
                }
            }
        }

        // Handle missing refresh token
        if (refresh == null) {
            ResponseUtil.createFilterErrorResponse(
                    response,
                    HttpStatus.BAD_REQUEST,
                    "Logout/TokenMissing",
                    "Refresh token is missing. Please provide a valid refresh token."
            );
            return;
        }

        // Handle expired refresh token
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            ResponseUtil.createFilterErrorResponse(
                    response,
                    HttpStatus.UNAUTHORIZED,
                    "Logout/TokenExpired",
                    "Refresh token has expired. Please provide a valid refresh token."
            );
            return;
        }

        // Validate token category
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            ResponseUtil.createFilterErrorResponse(
                    response,
                    HttpStatus.BAD_REQUEST,
                    "Logout/InvalidToken",
                    "Invalid token type. Expected a refresh token."
            );
            return;
        }

        // Check if token exists in the database
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {
            ResponseUtil.createFilterErrorResponse(
                    response,
                    HttpStatus.NOT_FOUND,
                    "Logout/TokenNotFound",
                    "Refresh token not found in the database. Please log in again."
            );
            return;
        }

        // Invalidate the refresh token cookie
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        ResponseUtil.createFilterSuccessResponse(response, null, HttpStatus.OK);
    }
}