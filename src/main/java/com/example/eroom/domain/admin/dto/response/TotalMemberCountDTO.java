package com.example.eroom.domain.admin.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class TotalMemberCountDTO {
    private LocalDate startDate;
    private Long totalMembers;

    public TotalMemberCountDTO(LocalDate startDate, Long totalMembers) {
        this.startDate = startDate;
        this.totalMembers = totalMembers;
    }
}
