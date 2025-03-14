package com.example.comitserver.controller;

import com.example.comitserver.config.jwt.JWTUtil;
import com.example.comitserver.dto.ServerResponseDTO;
import com.example.comitserver.dto.SignupRequestDTO;
import com.example.comitserver.dto.SignupResponseDTO;
import com.example.comitserver.entity.RefreshEntity;
import com.example.comitserver.entity.User;
import com.example.comitserver.exception.DuplicateResourceException;
import com.example.comitserver.repository.RefreshRepository;
import com.example.comitserver.service.auth.AuthService;
import com.example.comitserver.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Date;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;
    private final ModelMapper modelMapper;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @Autowired
    public AuthController(AuthService authService, ModelMapper modelMapper, JWTUtil jwtUtil, RefreshRepository refreshRepository) {
        this.authService = authService;
        this.modelMapper = modelMapper;
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> postUser(@RequestBody @Valid SignupRequestDTO signupRequestDTO, HttpServletResponse response) {
        User newUser = authService.signUp(signupRequestDTO);
        SignupResponseDTO userDTO = modelMapper.map(newUser, SignupResponseDTO.class);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/users/{id}")
                .buildAndExpand(newUser.getId())
                .toUri();

        Long userId = newUser.getId();

//        String accessToken = jwtUtil.createJwt("access", userId, 1800000L);
        String accessToken = jwtUtil.createJwt("access", userId, 18000000L);
        String refreshToken = jwtUtil.createJwt("refresh", userId, 1209600000L);

        Date date = new Date(System.currentTimeMillis() + 1209600000L);

        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setUserId(userId);
        refreshEntity.setRefresh(refreshToken);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);

        response.setHeader("access", accessToken);
        response.addCookie(JWTUtil.createCookie("refresh", refreshToken));

        return ResponseUtil.createSuccessResponse(userDTO, HttpStatus.CREATED, location);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<?> handleDuplicateResourceException(DuplicateResourceException ex, HttpServletRequest request) {
        String endpoint = ResponseUtil.extractEndpoint(request.getRequestURI());
        return ResponseUtil.createErrorResponse(
                HttpStatus.CONFLICT,
                endpoint + "/duplicate_resource",
                ex.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ServerResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String errorMessage = ex.getBindingResult().getAllErrors().stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Validation error");

        String endpoint = ResponseUtil.extractEndpoint(request.getRequestURI());

        return ResponseUtil.createErrorResponse(
                HttpStatus.BAD_REQUEST,
                endpoint + "/validation_failed",
                errorMessage
        );
    }
}
