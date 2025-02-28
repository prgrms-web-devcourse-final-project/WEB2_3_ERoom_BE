package com.example.eroom.domain.auth.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialLoginRequest {
    private String token;    // 소셜 로그인 토큰
    private String provider; // provider (google, kakao, naver 등)
}

