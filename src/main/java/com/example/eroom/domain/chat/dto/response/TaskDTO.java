package com.example.eroom.domain.chat.dto.response;

import com.example.eroom.domain.entity.TaskStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskDTO {

    private Long taskId;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private TaskStatus status;

    // 담당자 이름
    private String assignedMemberName;

    // 참여자 목록
    private List<String> participants;
}
