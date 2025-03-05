package com.example.eroom.domain.auth.controller;

import com.example.eroom.domain.auth.dto.request.SignupRequestDTO;
import com.example.eroom.domain.auth.dto.request.SocialLoginRequestDTO;
import com.example.eroom.domain.auth.dto.response.AuthResponseDTO;
import com.example.eroom.domain.auth.repository.RefreshTokenRepository;
import com.example.eroom.domain.auth.security.JwtTokenProvider;
import com.example.eroom.domain.auth.service.AuthService;
import com.example.eroom.domain.auth.service.TokenBlacklistService;
import com.example.eroom.domain.entity.Member;
import com.example.eroom.domain.entity.RefreshToken;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenBlacklistService tokenBlacklistService;

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

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        try {
            String newAccessToken = authService.refreshAccessToken(refreshToken);
            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @Transactional
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal Member member,
                                    @RequestHeader("Authorization") String authorizationHeader) {
        String accessToken = authorizationHeader.replace("Bearer ", "");

        // AccessToken을 블랙리스트에 추가 (남은 유효기간 동안)
        long expirationTimeMillis = jwtTokenProvider.getExpirationTime(accessToken); // 남은 시간 계산
        tokenBlacklistService.blacklistAccessToken(accessToken, expirationTimeMillis);

        // refreshToken 삭제
        refreshTokenRepository.deleteByEmail(member.getEmail());
        return ResponseEntity.ok("Logged out successfully");
    }
}