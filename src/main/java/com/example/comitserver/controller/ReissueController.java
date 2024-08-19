package com.example.comitserver.controller;

import com.example.comitserver.jwt.JWTUtil;
import com.example.comitserver.service.ReissueService;
import com.example.comitserver.uitls.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ReissueController {

    private final ReissueService reissueService;

    public ReissueController(ReissueService reissueService) {
        this.reissueService = reissueService;
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refresh = reissueService.getRefreshTokenFromCookies(request);

        if (refresh == null) {
            return ResponseUtil.createErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    "Reissue/TokenMissing",
                    "The request is missing the required refresh token. Please include a valid refresh token in cookie."
            );
        }

        if (reissueService.isTokenExpired(refresh)) {
            return ResponseUtil.createErrorResponse(
                    HttpStatus.UNAUTHORIZED,
                    "Reissue/TokenExpired",
                    "The provided refresh token has expired. Please request a new token or reauthenticate."
            );
        }

        if (!reissueService.isValidRefreshToken(refresh)) {
            return ResponseUtil.createErrorResponse(
                    HttpStatus.UNAUTHORIZED,
                    "Reissue/InvalidToken",
                    "The provided refresh token is invalid. Please provide a valid refresh token or reauthenticate."
            );
        }

        if (!reissueService.existsInDatabase(refresh)) {
            return ResponseUtil.createErrorResponse(
                    HttpStatus.NOT_FOUND,
                    "Reissue/TokenNotFound",
                    "The provided refresh token is invalid. Please provide a valid refresh token or reauthenticate."
            );
        }

        Long userId = reissueService.getUserIdFromToken(refresh);
        String newAccess = reissueService.createAccessToken(userId);
        String newRefresh = reissueService.createRefreshToken(userId);

        reissueService.updateRefreshTokenInDatabase(userId, refresh, newRefresh);

        response.setHeader("access", newAccess);
        response.addCookie(JWTUtil.createCookie("refresh", newRefresh));

        return ResponseUtil.createSuccessResponse(null);
    }
}
