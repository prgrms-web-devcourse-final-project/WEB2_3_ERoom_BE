package com.example.eroom.domain.chat.dto.response;

import com.example.eroom.domain.entity.Task;
import com.example.eroom.domain.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskListResponseDTO {

    private Long id;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private TaskStatus status;
    private Long assignedMemberId;
    private String assignedMemberName;
    private String assignedMemberProfile;
    private List<Long> participantIds;
    private List<String> participantProfiles;
    private Long projectId;

    // DTO 내에서 생성
    public static TaskListResponseDTO fromEntity(Task task) {
        return new TaskListResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getStartDate(),
                task.getEndDate(),
                task.getStatus(),
                task.getAssignedMember() != null ? task.getAssignedMember().getId() : null,
                task.getAssignedMember() != null ? task.getAssignedMember().getUsername() : null,
                task.getAssignedMember() != null ? task.getAssignedMember().getProfile() : null,
                task.getParticipants().stream()
                        .map(participant -> participant.getMember().getId())
                        .collect(Collectors.toList()),
                task.getParticipants().stream()
                        .map(participant -> participant.getMember().getProfile())
                        .collect(Collectors.toList()),
                task.getProject() != null ? task.getProject().getId() : null
        );
    }
}
