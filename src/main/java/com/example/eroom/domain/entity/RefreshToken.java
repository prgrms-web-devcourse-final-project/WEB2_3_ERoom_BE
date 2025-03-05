package com.example.eroom.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    private String email; // 회원의 email을 PK로 사용

    @Column(nullable = false, length = 500)
    private String token; // 리프레시 토큰 저장

    public void updateToken(String newToken) {
        this.token = newToken;
    }
}

