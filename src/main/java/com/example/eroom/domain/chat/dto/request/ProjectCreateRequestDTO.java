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
    private String category;
    private List<String> subCategories1;
    private List<String> subCategories2;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<Long> invitedMemberIds;
}
