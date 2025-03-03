package com.example.eroom.domain.auth.controller;

import com.example.eroom.domain.auth.dto.request.SignupRequestDTO;
import com.example.eroom.domain.auth.dto.request.SocialLoginRequestDTO;
import com.example.eroom.domain.auth.dto.response.AuthResponseDTO;
import com.example.eroom.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 소셜 로그인 처리
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody SocialLoginRequestDTO request) {
        AuthResponseDTO response = authService.login(request);

        // 비회원일 경우, non-member 정보만 반환
        if (!response.isRegistered()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        // 기존 회원일 경우 JWT와 회원정보를 반환
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDTO> signup(@ModelAttribute SignupRequestDTO request) {
        System.out.println("request :" + request.getIdToken());
        System.out.println("profileImage :" + request.getProfileImage());
        AuthResponseDTO response = authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}