package com.example.eroom.domain.admin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class MemberCountDTO {
    private LocalDate startDate;
    private Long totalMembers;

    public MemberCountDTO(LocalDate startDate, Long totalMembers) {
        this.startDate = startDate;
        this.totalMembers = totalMembers;
    }
}
