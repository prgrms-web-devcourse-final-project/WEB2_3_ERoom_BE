package com.example.eroom.domain.chat.service;

import com.example.eroom.domain.chat.dto.request.TaskCreateRequestDTO;
import com.example.eroom.domain.chat.dto.request.TaskUpdateRequestDTO;
import com.example.eroom.domain.chat.dto.response.TaskListResponseDTO;
import com.example.eroom.domain.chat.dto.response.TaskUpdateResponseDTO;
import com.example.eroom.domain.chat.error.CustomException;
import com.example.eroom.domain.chat.error.ErrorCode;
import com.example.eroom.domain.chat.repository.MemberRepository;
import com.example.eroom.domain.chat.repository.ProjectRepository;
import com.example.eroom.domain.chat.repository.TaskRepository;
import com.example.eroom.domain.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
                .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));

        // Task 객체 생성
        Task task = Task.builder()
                .title(requestDTO.getTitle())
                .startDate(requestDTO.getStartDate())
                .endDate(requestDTO.getEndDate())
                .status(requestDTO.getStatus())
                .project(project)
                .colors(requestDTO.getColors() != null ? requestDTO.getColors() : new ColorInfo("#FF5733", "#FFFFFF"))
                .createdAt(LocalDateTime.now())
                .deleteStatus(DeleteStatus.ACTIVE)
                .build();

        task = taskRepository.save(task);

        // 담당자 설정
        if (requestDTO.getAssignedMemberId() != null) {
            Member assignedMember = memberRepository.findById(requestDTO.getAssignedMemberId())
                    .orElseThrow(() -> new CustomException(ErrorCode.TASK_ASSIGNEE_NOT_FOUND));

            // 프로젝트에 속한 멤버인지 검증
            boolean isProjectMember = project.getMembers().stream()
                    .map(ProjectMember::getMember) // ProjectMember -> Member 변환
                    .anyMatch(member -> member.equals(assignedMember));

            if (!isProjectMember) {
                throw new CustomException(ErrorCode.TASK_ASSIGNEE_NOT_IN_PROJECT);
            }

            task.updateAssignedMember(assignedMember);

            // 담당자에게만 알림
            String message = "새로운 업무에 배정되었습니다: " + assignedMember.getUsername();
            notificationService.createNotification(assignedMember, message, NotificationType.TASK_ASSIGN, task.getId().toString() + " , " + task.getProject().getId().toString(), task.getTitle() + " , " + task.getProject().getName());// 알림생성, 저장, 알림 전송
        }

//        // 참여자 설정 -> 추후 도입
//        List<TaskMember> participants = requestDTO.getParticipantIds().stream()
//                .map(memberId -> {
//                    Member member = memberRepository.findById(memberId)
//                            .orElseThrow(() -> new CustomException(ErrorCode.TASK_PARTICIPANT_NOT_FOUND));
//                    return TaskMember.create(task, member);
//                })
//                .collect(Collectors.toList());
//
//        task.updateParticipants(participants);

//        // 알림 생성
//        for (TaskMember taskMember : participants) {
//            Member member = taskMember.getMember();
//            String message = "새로운 업무에 배정되었습니다: " + member.getUsername();
//            notificationService.createNotification(member, message, NotificationType.TASK_ASSIGN, task.getId(), task.getTitle());
//        }

        taskRepository.save(task);
    }

    public TaskUpdateResponseDTO getTask(Long taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException(ErrorCode.TASK_NOT_FOUND));

        return TaskUpdateResponseDTO.fromEntity(task);
    }

    public void updateTask(Long taskId, TaskUpdateRequestDTO requestDTO, Member editor) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException(ErrorCode.TASK_NOT_FOUND));

        // 수정 권한 체크
        if (!canEditTask(task, editor)) {
            throw new CustomException(ErrorCode.TASK_ACCESS_DENIED);
        }

        // Task 내용 변경
        //task.updateTask(requestDTO.getTitle(), requestDTO.getStartDate(), requestDTO.getEndDate(), requestDTO.getStatus());
        task = task.toBuilder()
                .title(requestDTO.getTitle() != null ? requestDTO.getTitle() : task.getTitle())
                .startDate(requestDTO.getStartDate() != null ? requestDTO.getStartDate() : task.getStartDate())
                .endDate(requestDTO.getEndDate() != null ? requestDTO.getEndDate() : task.getEndDate())
                .status(requestDTO.getStatus() != null ? requestDTO.getStatus() : task.getStatus())
                .build();

//        // 참여자 변경
//        List<TaskMember> newParticipants = requestDTO.getParticipantIds().stream()
//                .map(memberId -> {
//                    Member member = memberRepository.findById(memberId)
//                            .orElseThrow(() -> new CustomException(ErrorCode.TASK_PARTICIPANT_NOT_FOUND));
//                    return TaskMember.create(task, member);
//                })
//                .collect(Collectors.toList());
//
//        task.updateParticipants(newParticipants);

        // 담당자 변경 (참여자 목록에 있어야만 설정 가능) -> 참여자를 고려 x 프로젝트의 멤버인지만 검증
        if (requestDTO.getAssignedMemberId() != null) {
//            // 참여자 목록에 속한 멤버인지 검증하는 로직
//            boolean isAssignedInParticipants = newParticipants.stream()
//                    .anyMatch(participant -> participant.getMember().getId().equals(requestDTO.getAssignedMemberId()));
//
//            if (!isAssignedInParticipants) {
//                throw new CustomException(ErrorCode.TASK_ASSIGNEE_MUST_BE_PARTICIPANT);
//            }

            Member assignedMember = memberRepository.findById(requestDTO.getAssignedMemberId())
                    .orElseThrow(() -> new CustomException(ErrorCode.TASK_ASSIGNEE_NOT_FOUND));

            // 프로젝트에 속한 멤버인지 검증
            boolean isProjectMember = task.getProject().getMembers().stream()
                    .map(ProjectMember::getMember) // ProjectMember -> Member 변환
                    .anyMatch(member -> member.equals(assignedMember));

            if (!isProjectMember) {
                throw new CustomException(ErrorCode.TASK_ASSIGNEE_NOT_IN_PROJECT);
            }

            task.updateAssignedMember(assignedMember);
        } else {
            task.updateAssignedMember(null);
        }

        taskRepository.save(task);
    }

    public void deleteTask(Long taskId, Member editor) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException(ErrorCode.TASK_NOT_FOUND));

        // 삭제 권한 체크
        if (!canEditTask(task, editor)) {
            throw new CustomException(ErrorCode.TASK_ACCESS_DENIED);
        }

        // soft delete
        task.deleteTask();
        taskRepository.save(task);
    }

    public List<TaskListResponseDTO> getTasksByMember(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // deleteStatus=ACTIVE인 task만 조회
        List<Task> tasks = taskRepository.findByAssignedMemberAndDeleteStatus(member, DeleteStatus.ACTIVE);

        return tasks.stream()
                .map(TaskListResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // Task 수정 권한
    private boolean canEditTask(Task task, Member editor) {
        return (task.getAssignedMember() != null && task.getAssignedMember().getId().equals(editor.getId()))
                || task.getProject().getCreator().getId().equals(editor.getId());
    }
}
