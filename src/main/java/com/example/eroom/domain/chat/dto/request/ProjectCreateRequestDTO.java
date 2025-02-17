package com.example.eroom.domain.chat.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProjectCreateRequestDTO {

    private String name;
    private String description;
    private String tag1;
    private String tag2;
    private String tag3;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<Long> invitedMemberIds;
}
