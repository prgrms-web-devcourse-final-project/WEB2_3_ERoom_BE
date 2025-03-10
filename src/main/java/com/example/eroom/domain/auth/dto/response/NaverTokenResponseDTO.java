package com.example.eroom.domain.auth.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NaverTokenResponseDTO {
    private NaverAccount response;

    @Getter
    @Setter
    public static class NaverAccount {
        private String email;
    }
}
