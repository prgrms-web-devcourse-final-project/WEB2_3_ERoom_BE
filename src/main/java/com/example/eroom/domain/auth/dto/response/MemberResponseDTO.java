package com.example.eroom.domain.auth.dto.response;

import com.example.eroom.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberResponseDTO {
    private Long id;
    private String email;
    private String username;
    private String organization;
    private String profile;

    public MemberResponseDTO(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.username = member.getUsername();
        this.organization = member.getOrganization();
        this.profile = member.getProfile();
    }
}

