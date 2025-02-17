package com.example.eroom.domain.chat.thymeleaf.service;

import com.example.eroom.domain.chat.repository.ProjectRepository;
import com.example.eroom.domain.chat.repository.TaskRepository;
import com.example.eroom.domain.chat.repository.MemberRepository;
import com.example.eroom.entity.Project;
import com.example.eroom.entity.Task;
import com.example.eroom.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;

    public List<Task> getTasksByProject(Project project) {
        return taskRepository.findByProject(project);
    }

    public void addTaskToProject(Long projectId, String title, String description, Long assignedUserId,
                                 LocalDateTime startDate, LocalDateTime endDate) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project Id: " + projectId));

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setProject(project);

        task.setStartDate(startDate);
        task.setEndDate(endDate);

        if (assignedUserId != null) {
            Member assignedMember = memberRepository.findById(assignedUserId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + assignedUserId));
            task.setAssignedMember(assignedMember);
        }

        taskRepository.save(task);
    }
}