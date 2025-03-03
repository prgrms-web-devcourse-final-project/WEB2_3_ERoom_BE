package com.example.eroom.domain.chat.dto.response;

import com.example.eroom.domain.entity.DeleteStatus;
import com.example.eroom.domain.entity.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectSearchResponseDTO {

    private Long projectId;
    private String projectName;
    private String creatorName;
    private String creatorEmail;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;
    private ProjectStatus projectStatus;
    private DeleteStatus deleteStatus;
}
