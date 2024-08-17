package com.example.comitserver.service;

import com.example.comitserver.entity.RefreshEntity;
import com.example.comitserver.jwt.JWTUtil;
import com.example.comitserver.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ReissueService {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public ReissueService(JWTUtil jwtUtil, RefreshRepository refreshRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

    public String getRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public boolean isTokenExpired(String refreshToken) {
        try {
            jwtUtil.isExpired(refreshToken);
            return false;
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public boolean isValidRefreshToken(String refreshToken) {
        String category = jwtUtil.getCategory(refreshToken);
        return category.equals("refresh");
    }

    public boolean existsInDatabase(String refreshToken) {
        return refreshRepository.existsByRefresh(refreshToken);
    }

    public Long getUserIdFromToken(String refreshToken) {
        return jwtUtil.getUserId(refreshToken);
    }

    public String createAccessToken(Long userId) {
        return jwtUtil.createJwt("access", userId, 1800000L);
    }

    public String createRefreshToken(Long userId) {
        return jwtUtil.createJwt("refresh", userId, 1209600000L);
    }

    public void updateRefreshTokenInDatabase(Long userId, String oldRefreshToken, String newRefreshToken) {
        refreshRepository.deleteByRefresh(oldRefreshToken);
        addRefreshEntity(userId, newRefreshToken);
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
