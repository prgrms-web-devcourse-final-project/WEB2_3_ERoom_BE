package com.example.eroom.domain.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {
    private List<TotalMemberCountDTO> totalMembers;
    private List<NewMemberCountDTO> newMembers;
}
