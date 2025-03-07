package com.example.eroom.domain.admin.dto.response;

import com.example.eroom.domain.entity.Task;
import com.example.eroom.domain.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminTaskDTO {
    private Long taskId;
    private String taskName;
    private String projectName;
    private String assignedMember;
    private String assignedEmail;
    private TaskStatus taskStatus;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    // Task 엔티티를 매개변수로 받는 생성자
    public AdminTaskDTO(Task task) {
        this.taskId = task.getId();
        this.taskName = task.getTitle();
        this.projectName = task.getProject().getName();
        this.assignedMember = task.getAssignedMember().getUsername();
        this.assignedEmail = task.getAssignedMember().getEmail();
        this.taskStatus = task.getStatus();
        this.startDate = task.getStartDate();
        this.endDate = task.getEndDate();
    }
}
