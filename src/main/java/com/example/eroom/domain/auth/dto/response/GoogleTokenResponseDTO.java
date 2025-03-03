package com.example.eroom.domain.auth.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleTokenResponseDTO {
    private String email;
    private String sub;
}

