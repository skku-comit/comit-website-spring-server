package com.example.comitserver.controller;

import com.example.comitserver.dto.ServerErrorDTO;
import com.example.comitserver.dto.ServerResponseDTO;
import com.example.comitserver.jwt.JWTUtil;
import com.example.comitserver.service.ReissueService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

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
            return new ResponseEntity<>(
                    new ServerResponseDTO(
                            new ServerErrorDTO(
                                    "400 Bad Request",
                                    "Refresh token missing",
                                    "The request is missing the required refresh token. Please include a valid refresh token in cookie."
                            ), null
                    ), HttpStatus.BAD_REQUEST
            );
        }

        if (reissueService.isTokenExpired(refresh)) {
            return new ResponseEntity<>(
                    new ServerResponseDTO(
                            new ServerErrorDTO(
                                    "401 Unauthorized",
                                    "Refresh token expired",
                                    "The provided refresh token has expired. Please request a new token or reauthenticate."
                            ), null
                    ), HttpStatus.UNAUTHORIZED
            );
        }

        if (!reissueService.isValidRefreshToken(refresh)) {
            return new ResponseEntity<>(
                    new ServerResponseDTO(
                            new ServerErrorDTO(
                                    "401 Unauthorized",
                                    "Invalid refresh token",
                                    "The provided refresh token is invalid. Please provide a valid refresh token or reauthenticate."
                            ), null
                    ), HttpStatus.UNAUTHORIZED
            );
        }

        if (!reissueService.existsInDatabase(refresh)) {
            return new ResponseEntity<>(
                    new ServerResponseDTO(
                            new ServerErrorDTO(
                                    "404 Not Found",
                                    "Invalid refresh token",
                                    "The provided refresh token is invalid. Please provide a valid refresh token or reauthenticate."
                            ), null
                    ), HttpStatus.NOT_FOUND
            );
        }

        Long userId = reissueService.getUserIdFromToken(refresh);
        String newAccess = reissueService.createAccessToken(userId);
        String newRefresh = reissueService.createRefreshToken(userId);

        reissueService.updateRefreshTokenInDatabase(userId, refresh, newRefresh);

        response.setHeader("access", newAccess);
        response.addCookie(JWTUtil.createCookie("refresh", newRefresh));

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Reissue Successful");

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
