package com.example.eroom.domain.chat.dto.response;

import com.example.eroom.domain.entity.ProjectStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectDetailDTO {

    private Long projectId;
    private String projectName;
    private String description;
    private String tag1;
    private String tag2;
    private String tag3;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ProjectStatus status;
    private ChatRoomDTO groupChatRoom;
}
