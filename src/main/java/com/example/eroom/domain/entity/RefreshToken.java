package com.example.eroom.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class RefreshToken {

    @Id
    private String email; // 회원의 email을 PK로 사용

    @Column(nullable = false, length = 500)
    private String token; // 리프레시 토큰 저장

    @Column(nullable = false)
    private LocalDateTime expiredAt; // 만료시간

    @Builder
    public RefreshToken(String email, String token, LocalDateTime expiredAt) {
        this.email = email;
        this.token = token;
        this.expiredAt = expiredAt;
    }

    public void updateToken(String newToken, LocalDateTime newExpiresAt) {
        this.token = newToken;
        this.expiredAt = newExpiresAt;
    }
}

