package com.example.eroom.domain.chat.dto.request;

import com.example.eroom.domain.entity.TaskStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskCreateRequestDTO {

    private Long projectId;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private TaskStatus status;
    private Long assignedMemberId;
    private List<Long> participantIds;
}
