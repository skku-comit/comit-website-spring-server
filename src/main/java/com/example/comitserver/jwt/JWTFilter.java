package com.example.comitserver.jwt;

import com.example.comitserver.service.CustomUserDetailsService;
import com.example.comitserver.utils.ResponseUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {
    private final CustomUserDetailsService customUserDetailsService;

    private final JWTUtil jwtUtil;
    public JWTFilter(JWTUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Bearer 이후의 토큰 부분만 추출
        String accessToken = authorizationHeader.substring(7);

        // URI에서 엔드포인트 추출
        String endpoint = extractEndpoint(request.getRequestURI());

        // 토큰 만료 여부 확인, 만료 시 다음 필터로 넘기지 않음
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            ResponseUtil.writeErrorResponse(response, HttpStatus.UNAUTHORIZED, endpoint + "/token_expired", "Access token expired");
            return;
        } catch (JwtException e) {
            ResponseUtil.writeErrorResponse(response, HttpStatus.UNAUTHORIZED, endpoint + "/invalid_token", "Invalid JWT token");
            return;
        }

        // 토큰이 access인지 확인 (발급 시 페이로드에 명시)
        String category = jwtUtil.getCategory(accessToken);
        if (!category.equals("access")) {
            ResponseUtil.writeErrorResponse(response, HttpStatus.UNAUTHORIZED, endpoint + "/invalid_access_token", "Invalid access token");
            return;
        }

        Long userId = jwtUtil.getUserId(accessToken);

        if (userId == null) {
            ResponseUtil.writeErrorResponse(response, HttpStatus.UNAUTHORIZED, endpoint + "/user_id_not_found", "User ID not found in token");
            return;
        }

        UserDetails userDetails = customUserDetailsService.loadUserById(userId);

        if (userDetails == null) {
            ResponseUtil.writeErrorResponse(response, HttpStatus.UNAUTHORIZED, endpoint + "/user_not_found", "User not found");
            return;
        }

        Authentication authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
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