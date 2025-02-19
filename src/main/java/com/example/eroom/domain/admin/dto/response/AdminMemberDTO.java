package com.example.eroom.domain.admin.dto.response;

import com.example.eroom.domain.entity.MemberGrade;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class AdminMemberDTO {
    private String email;
    private String username;
    private String organization;
    private String profile;
    private LocalDate createdAt;
    private MemberGrade memberGrade;
}
