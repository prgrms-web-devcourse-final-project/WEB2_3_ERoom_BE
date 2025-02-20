package com.example.eroom.domain.chat.dto.response;

import com.example.eroom.domain.entity.ProjectStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProjectDetailChatDTO {

    private Long projectId;
    private String projectName;
    private String description;
    private String category;
    private List<String> subCategories1;
    private List<String> subCategories2;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ProjectStatus status;
    private ChatRoomDTO groupChatRoom;
}
