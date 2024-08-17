package com.example.comitserver.controller;

import com.example.comitserver.jwt.JWTUtil;
import com.example.comitserver.service.ReissueService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReissueController {

    private final ReissueService reissueService;

    public ReissueController(ReissueService reissueService) {
        this.reissueService = reissueService;
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refresh = reissueService.getRefreshTokenFromCookies(request);

        if (refresh == null) {
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        if (reissueService.isTokenExpired(refresh)) {
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        if (!reissueService.isValidRefreshToken(refresh)) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        if (!reissueService.existsInDatabase(refresh)) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        Long userId = reissueService.getUserIdFromToken(refresh);
        String newAccess = reissueService.createAccessToken(userId);
        String newRefresh = reissueService.createRefreshToken(userId);

        reissueService.updateRefreshTokenInDatabase(userId, refresh, newRefresh);

        response.setHeader("access", newAccess);
        response.addCookie(JWTUtil.createCookie("refresh", newRefresh));

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
