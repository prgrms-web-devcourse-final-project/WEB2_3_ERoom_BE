package com.example.eroom.domain.chat.dto.response;

import com.example.eroom.domain.entity.ProjectStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectDetailChatDTO {

    private Long projectId;
    private String projectName;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ProjectStatus status;
    private ChatRoomDTO groupChatRoom;
}
