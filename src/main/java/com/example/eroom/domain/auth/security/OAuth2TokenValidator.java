package com.example.eroom.domain.auth.security;

import com.example.eroom.domain.auth.dto.request.OAuth2UserInfoDTO;
import com.example.eroom.domain.auth.dto.response.GoogleTokenResponseDTO;
import com.example.eroom.domain.auth.dto.response.KakaoTokenResponseDTO;
import com.example.eroom.domain.auth.dto.response.NaverTokenResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OAuth2TokenValidator {

    private final RestTemplate restTemplate;
    private final JwtDecoder jwtDecoder;

    public OAuth2UserInfoDTO validateToken(String provider, String token) {
        System.out.println("OAuth2 Token Validation 시작 - Provider: " + provider + ", Token: " + token);

        OAuth2UserInfoDTO userInfo = new OAuth2UserInfoDTO();
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
            System.out.println("OAuth2 토큰 검증 실패 - 예외 발생: " + e.getMessage());
            return null;
        }
    }


    private OAuth2UserInfoDTO validateGoogleToken(String token, OAuth2UserInfoDTO userInfo) {
        String googleUrl = "https://oauth2.googleapis.com/tokeninfo?id_token=" + token;
        try {
            System.out.println("Google Token 검증 요청 URL: " + googleUrl);

            GoogleTokenResponseDTO response = restTemplate.getForObject(googleUrl, GoogleTokenResponseDTO.class);

            if (response != null) {
                System.out.println("Google Token 검증 성공: " + response);
                System.out.println("Google Token: " + token);
                userInfo.setEmail(response.getEmail());
                userInfo.setProvider("google");
                userInfo.setIdToken(token);

                System.out.println("OAuth2UserInfoDTO 생성됨: " + userInfo);
            } else {
                System.out.println("Google Token 검증 실패 - 응답이 null");
            }
        } catch (Exception e) {
            System.out.println("Google Token 검증 중 예외 발생: " + e.getMessage());
            return null;
        }
        return userInfo;
    }


    private OAuth2UserInfoDTO validateNaverToken(String token, OAuth2UserInfoDTO userInfo) {
        String naverUrl = "https://openapi.naver.com/v1/nid/me";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<?> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<NaverTokenResponseDTO> response = restTemplate.exchange(naverUrl, HttpMethod.GET, request, NaverTokenResponseDTO.class);
            if (response.getBody() != null) {
                userInfo.setEmail(response.getBody().getResponse().getEmail());
                userInfo.setProvider("naver");  // provider 설정
            }
        } catch (Exception e) {
            return null;
        }
        return userInfo;
    }

    private OAuth2UserInfoDTO validateKakaoToken(String token, OAuth2UserInfoDTO userInfo) {
        String kakaoUrl = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<?> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<KakaoTokenResponseDTO> response = restTemplate.exchange(kakaoUrl, HttpMethod.GET, request, KakaoTokenResponseDTO.class);
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

    /*public OAuth2UserInfoDTO validateGoogleToken(String idToken) {
        Jwt jwt = jwtDecoder.decode(idToken);
        String oauthId = jwt.getClaimAsString("sub");
        String email = jwt.getClaimAsString("email");
        String provider = jwt.getClaimAsString("provider");

        return new OAuth2UserInfoDTO(oauthId, email, provider);
    }*/
}


