package com.example.eroom.domain.chat.dto.response;

import com.example.eroom.domain.entity.Task;
import com.example.eroom.domain.entity.TaskStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskUpdateResponseDTO {
    private Long id;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private TaskStatus status;
    private Long assignedMemberId;
    private List<Long> participantIds;

    // DTO 내에서 생성
    public static TaskUpdateResponseDTO fromEntity(Task task) {
        return new TaskUpdateResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getStartDate(),
                task.getEndDate(),
                task.getStatus(),
                task.getAssignedMember() != null ? task.getAssignedMember().getId() : null,
                task.getParticipants().stream()
                        .map(participant -> participant.getMember().getId())
                        .collect(Collectors.toList())
        );
    }
}
