package com.example.eroom.domain.auth.dto.response;

import com.example.eroom.domain.auth.dto.request.OAuth2UserInfoDTO;
import com.example.eroom.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponseDTO {
    private boolean isRegistered;  // 기존 회원 여부
    private String accessToken;    // JWT 엑세스 토큰
    private String refreshToken;   // JWT 리프레시 토큰
    private Member member;         // 회원 정보

    // 기존 회원 응답 생성 메서드
    public static AuthResponseDTO ofExistingUser(Member member, String accessToken, String refreshToken) {
        return new AuthResponseDTO(true, accessToken, refreshToken, member);
    }

    // 신규 회원 응답 생성 메서드
    public static AuthResponseDTO ofNewUser(OAuth2UserInfoDTO userInfo) {
        return new AuthResponseDTO(false, null, null, null);
    }
}



