package com.example.eroom.domain.chat.service;

import com.example.eroom.domain.chat.dto.request.TaskCreateRequestDTO;
import com.example.eroom.domain.chat.dto.request.TaskUpdateRequestDTO;
import com.example.eroom.domain.chat.dto.response.TaskUpdateResponseDTO;
import com.example.eroom.domain.chat.repository.MemberRepository;
import com.example.eroom.domain.chat.repository.ProjectRepository;
import com.example.eroom.domain.chat.repository.TaskRepository;
import com.example.eroom.domain.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    public void createTask(TaskCreateRequestDTO requestDTO) {
        Project project = projectRepository.findById(requestDTO.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("프로젝트가 존재하지 않습니다."));

        Task task = new Task();
        task.setTitle(requestDTO.getTitle());
        task.setStartDate(requestDTO.getStartDate());
        task.setEndDate(requestDTO.getEndDate());
        task.setStatus(requestDTO.getStatus());
        task.setProject(project);

        // 담당자 설정
        if (requestDTO.getAssignedMemberId() != null) {
            Member assignedMember = memberRepository.findById(requestDTO.getAssignedMemberId())
                    .orElseThrow(() -> new IllegalArgumentException("담당자가 존재하지 않습니다."));
            task.setAssignedMember(assignedMember);
        }

        // 참여자 설정
        List<TaskMember> participants = requestDTO.getParticipantIds().stream()
                .map(memberId -> {
                    Member member = memberRepository.findById(memberId)
                            .orElseThrow(() -> new IllegalArgumentException("참여자가 존재하지 않습니다."));
                    TaskMember taskMember = new TaskMember();
                    taskMember.setTask(task);
                    taskMember.setMember(member);
                    return taskMember;
                }).collect(Collectors.toList());

        for(TaskMember taskMember : participants){
            Member member = taskMember.getMember();
            String message = "새로운 업무에 배정되었습니다: " + member.getUsername();
            notificationService.createNotification(member, message, NotificationType.TASK_ASSIGN, taskMember.getTask().getId());
        }

        task.setParticipants(participants);
        taskRepository.save(task);
    }

    public TaskUpdateResponseDTO getTask(Long taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("업무가 존재하지 않습니다."));

        return TaskUpdateResponseDTO.fromEntity(task);
    }

    public void updateTask(Long taskId, TaskUpdateRequestDTO requestDTO, Member editor) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("업무가 존재하지 않습니다."));

        // 수정 권한 체크: Task 담당자 or 프로젝트 생성자만 가능
        if (!canEditTask(task, editor)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        // 제목, 날짜, 상태 변경
        task.setTitle(requestDTO.getTitle());
        task.setStartDate(requestDTO.getStartDate());
        task.setEndDate(requestDTO.getEndDate());
        task.setStatus(requestDTO.getStatus());

        // 참여자 변경
        List<TaskMember> newParticipants = requestDTO.getParticipantIds().stream()
                .map(memberId -> {
                    Member member = memberRepository.findById(memberId)
                            .orElseThrow(() -> new IllegalArgumentException("참여자가 존재하지 않습니다."));
                    TaskMember taskMember = new TaskMember();
                    taskMember.setTask(task);
                    taskMember.setMember(member);
                    return taskMember;
                }).collect(Collectors.toList());

        task.getParticipants().clear();
        task.getParticipants().addAll(newParticipants);

        // 담당자 변경 (참여자 목록에 있어야만 설정 가능)
        if (requestDTO.getAssignedMemberId() != null) {
            boolean isAssignedInParticipants = newParticipants.stream()
                    .anyMatch(participant -> participant.getMember().getId().equals(requestDTO.getAssignedMemberId()));

            if (!isAssignedInParticipants) {
                throw new IllegalArgumentException("담당자는 참여자 목록에 있어야 합니다.");
            }

            Member assignedMember = memberRepository.findById(requestDTO.getAssignedMemberId())
                    .orElseThrow(() -> new IllegalArgumentException("담당자가 존재하지 않습니다."));
            task.setAssignedMember(assignedMember);
        } else {
            task.setAssignedMember(null);
        }

        taskRepository.save(task);
    }

    // Task 수정 권한
    private boolean canEditTask(Task task, Member editor) {
        return (task.getAssignedMember() != null && task.getAssignedMember().getId().equals(editor.getId()))
                || task.getProject().getCreator().getId().equals(editor.getId());
    }
}
