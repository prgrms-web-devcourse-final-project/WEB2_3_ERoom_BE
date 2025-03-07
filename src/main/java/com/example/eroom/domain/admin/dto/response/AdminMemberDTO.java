package com.example.eroom.domain.admin.dto.response;

import com.example.eroom.domain.entity.Member;
import com.example.eroom.domain.entity.MemberGrade;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminMemberDTO {
    private Long memberId;
    private String email;
    private String username;
    private String organization;
    private String profile;
    private LocalDate createdAt;

    // Member 엔티티를 매개변수로 받는 생성자
    public AdminMemberDTO(Member member) {
        this.memberId = member.getId();
        this.email = member.getEmail();
        this.username = member.getUsername();
        this.organization = member.getOrganization();
        this.profile = member.getProfile();
        this.createdAt = member.getCreatedAt();
    }
}
