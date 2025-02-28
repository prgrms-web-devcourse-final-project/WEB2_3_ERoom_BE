package com.example.eroom.domain.auth.security;

import com.example.eroom.domain.auth.dto.request.OAuth2UserInfo;
import com.example.eroom.domain.auth.dto.response.GoogleTokenResponse;
import com.example.eroom.domain.auth.dto.response.KakaoTokenResponse;
import com.example.eroom.domain.auth.dto.response.NaverTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OAuth2TokenValidator {

    private final RestTemplate restTemplate;

    public OAuth2UserInfo validateToken(String provider, String token) {
        System.out.println("🔍 OAuth2 Token Validation 시작 - Provider: " + provider + ", Token: " + token);

        OAuth2UserInfo userInfo = new OAuth2UserInfo();
        try {
            switch (provider.toLowerCase()) {
                case "google":
                    return validateGoogleToken(token, userInfo);
                case "naver":
                    return validateNaverToken(token, userInfo);
                case "kakao":
                    return validateKakaoToken(token, userInfo);
                default:
                    throw new IllegalArgumentException("지원되지 않는 OAuth Provider입니다: " + provider);
            }
        } catch (Exception e) {
            System.out.println("❌ OAuth2 토큰 검증 실패 - 예외 발생: " + e.getMessage());
            return null;
        }
    }


    private OAuth2UserInfo validateGoogleToken(String token, OAuth2UserInfo userInfo) {
        String googleUrl = "https://oauth2.googleapis.com/tokeninfo?id_token=" + token;
        try {
            System.out.println("🔍 Google Token 검증 요청 URL: " + googleUrl);

            GoogleTokenResponse response = restTemplate.getForObject(googleUrl, GoogleTokenResponse.class);

            if (response != null) {
                System.out.println("✅ Google Token 검증 성공: " + response);
                userInfo.setEmail(response.getEmail());
                userInfo.setProvider("google");
            } else {
                System.out.println("❌ Google Token 검증 실패 - 응답이 null");
            }
        } catch (Exception e) {
            System.out.println("❌ Google Token 검증 중 예외 발생: " + e.getMessage());
            return null;
        }
        return userInfo;
    }


    private OAuth2UserInfo validateNaverToken(String token, OAuth2UserInfo userInfo) {
        String naverUrl = "https://openapi.naver.com/v1/nid/me";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<?> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<NaverTokenResponse> response = restTemplate.exchange(naverUrl, HttpMethod.GET, request, NaverTokenResponse.class);
            if (response.getBody() != null) {
                userInfo.setEmail(response.getBody().getResponse().getEmail());
                userInfo.setProvider("naver");  // provider 설정
            }
        } catch (Exception e) {
            return null;
        }
        return userInfo;
    }

    private OAuth2UserInfo validateKakaoToken(String token, OAuth2UserInfo userInfo) {
        String kakaoUrl = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<?> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<KakaoTokenResponse> response = restTemplate.exchange(kakaoUrl, HttpMethod.GET, request, KakaoTokenResponse.class);
            if (response.getBody() != null) {
                userInfo.setEmail(response.getBody().getKakaoAccount().getEmail());
                userInfo.setProvider("kakao");  // provider 설정
            }
        } catch (Exception e) {
            System.out.println("Google token validation failed: " +  e.getMessage());
            return null;
        }
        return userInfo;
    }
}


