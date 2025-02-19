package com.example.eroom.domain.chat.service;

import com.example.eroom.domain.chat.dto.request.TaskCreateRequestDTO;
import com.example.eroom.domain.chat.repository.MemberRepository;
import com.example.eroom.domain.chat.repository.ProjectRepository;
import com.example.eroom.domain.chat.repository.TaskRepository;
import com.example.eroom.domain.entity.Member;
import com.example.eroom.domain.entity.Project;
import com.example.eroom.domain.entity.Task;
import com.example.eroom.domain.entity.TaskMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;

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

        task.setParticipants(participants);
        taskRepository.save(task);
    }
}
