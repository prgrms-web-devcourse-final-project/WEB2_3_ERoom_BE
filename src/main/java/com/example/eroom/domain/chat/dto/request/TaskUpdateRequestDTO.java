package com.example.eroom.domain.chat.dto.request;

import com.example.eroom.domain.entity.TaskStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskUpdateRequestDTO {
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private TaskStatus status;
    private Long assignedMemberId; // 담당자 (participants 중에서 선택 가능)
    private List<Long> participantIds; // 참여자 목록
}
