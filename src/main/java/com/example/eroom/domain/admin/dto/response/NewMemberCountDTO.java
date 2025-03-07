package com.example.eroom.domain.admin.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class NewMemberCountDTO {
    private LocalDate date;
    private Long newMembers;

    public NewMemberCountDTO(LocalDate date, Long newMembers) {
        this.date = date;
        this.newMembers = newMembers;
    }
}
