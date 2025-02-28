package com.example.eroom.domain.auth.dto.response;

import com.example.eroom.domain.auth.dto.request.OAuth2UserInfo;
import com.example.eroom.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {
    private boolean isRegistered;  // 기존 회원 여부
    private String accessToken;    // JWT 엑세스 토큰
    private String refreshToken;   // JWT 리프레시 토큰
    private Member member;         // 회원 정보

    // 기존 회원 응답 생성 메서드 (리프레시 토큰 추가)
    public static AuthResponse ofExistingUser(Member member, String accessToken, String refreshToken) {
        return new AuthResponse(true, accessToken, refreshToken, member);
    }

    // 신규 회원 응답 생성 메서드 (변경 없음)
    public static AuthResponse ofNewUser(OAuth2UserInfo userInfo) {
        return new AuthResponse(false, null, null, null);
    }
}



