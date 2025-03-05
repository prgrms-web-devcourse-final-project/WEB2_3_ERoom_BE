package com.example.eroom.domain.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskSearchResponseDTO {

    private Long taskId;
    private String taskName;
    private String projectName;
    private String assignedMemberName;
    private String assignedMemberEmail;
    private String taskStatus;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
