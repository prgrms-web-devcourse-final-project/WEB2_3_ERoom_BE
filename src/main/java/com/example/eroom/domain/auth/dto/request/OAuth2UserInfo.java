package com.example.eroom.domain.auth.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuth2UserInfo {
    private String email;
    private String provider; // 추가

    // 생성자나 필요한 다른 필드들을 추가할 수 있음
}


