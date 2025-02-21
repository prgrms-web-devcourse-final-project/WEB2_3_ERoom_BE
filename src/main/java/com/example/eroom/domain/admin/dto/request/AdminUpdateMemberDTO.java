package com.example.eroom.domain.admin.dto.request;

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
public class AdminUpdateMemberDTO {
    private String username;
    private LocalDate createdAt;
    private MemberGrade memberGrade;
    private String organization;
    private String profile;

}
