package com.example.eroom.domain.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class MemberSearchResponseDTO {

    private Long id;
    private String username;
    private String email;
    private String profile;
}
