package com.example.eroom.domain.chat.service;

import com.example.eroom.domain.chat.dto.response.MemberSearchResponseDTO;
import com.example.eroom.domain.chat.dto.response.ProjectSearchResponseDTO;
import com.example.eroom.domain.chat.dto.response.TaskSearchResponseDTO;
import com.example.eroom.domain.chat.repository.MemberRepository;
import com.example.eroom.domain.chat.repository.ProjectRepository;
import com.example.eroom.domain.chat.repository.TaskRepository;
import com.example.eroom.domain.entity.DeleteStatus;
import com.example.eroom.domain.entity.Member;
import com.example.eroom.domain.entity.Project;
import com.example.eroom.domain.entity.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    // 멤버 검색
    public List<MemberSearchResponseDTO> searchMembersByName(String name) {

        List<Member> members = memberRepository.findByUsernameContainingIgnoreCaseAndDeleteStatus(name, DeleteStatus.ACTIVE);

        return members.stream()
                .map(member -> new MemberSearchResponseDTO(
                        member.getId(),
                        member.getUsername(),
                        member.getEmail(),
                        member.getProfile(),
                        member.getOrganization(),
                        member.getCreatedAt(),
                        member.getDeleteStatus()
                ))
                .collect(Collectors.toList());
    }

    // 관리자 멤버 검색
    public List<MemberSearchResponseDTO> adminSearchMembersByName(String name) {

        List<Member> members = memberRepository.findByUsernameContainingIgnoreCase(name);

        return members.stream()
                .map(member -> new MemberSearchResponseDTO(
                        member.getId(),
                        member.getUsername(),
                        member.getEmail(),
                        member.getProfile(),
                        member.getOrganization(),
                        member.getCreatedAt(),
                        member.getDeleteStatus()
                ))
                .collect(Collectors.toList());
    }

    // 프로젝트 검색
    public List<ProjectSearchResponseDTO> searchProjectsByName(String name) {

        List<Project> projects = projectRepository.findByNameContainingIgnoreCase(name);

        return projects.stream()
                .map(project -> new ProjectSearchResponseDTO(
                        project.getId(),
                        project.getName(),
                        project.getCreator().getUsername(),
                        project.getCreator().getEmail(),
                        project.getStartDate(),
                        project.getEndDate(),
                        project.getCreatedAt(),
                        project.getStatus(),
                        project.getDeleteStatus()
                ))
                .collect(Collectors.toList());
    }

    // Task 검색
    public List<TaskSearchResponseDTO> searchTasksByName(String title) {

        List<Task> tasks = taskRepository.findByTitleContainingIgnoreCase(title);

        return tasks.stream()
                .map(task -> new TaskSearchResponseDTO(
                        task.getId(),
                        task.getTitle(),
                        task.getProject() != null ? task.getProject().getName() : null,
                        task.getAssignedMember() != null ? task.getAssignedMember().getUsername() : null,
                        task.getAssignedMember() != null ? task.getAssignedMember().getEmail() : null,
                        task.getStatus().name(),
                        task.getStartDate(),
                        task.getEndDate()
                ))
                .collect(Collectors.toList());
    }
}
