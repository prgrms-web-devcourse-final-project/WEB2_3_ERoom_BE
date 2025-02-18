package com.example.eroom.domain.chat.dto.response;

import com.example.eroom.entity.ProjectStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProjectDetailDTO {

    private Long projectId;
    private String projectName;

    // Task 목록
    private List<TaskDTO> tasks;
}
