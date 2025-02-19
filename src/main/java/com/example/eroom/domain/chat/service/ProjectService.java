package com.example.eroom.domain.chat.service;

import com.example.eroom.domain.chat.dto.request.ProjectCreateRequestDTO;
import com.example.eroom.domain.chat.dto.request.ProjectUpdateRequestDTO;
import com.example.eroom.domain.chat.dto.response.*;
import com.example.eroom.domain.chat.repository.MemberRepository;
import com.example.eroom.domain.chat.repository.ProjectRepository;
import com.example.eroom.domain.entity.*;
import com.example.eroom.domain.chat.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    // 현재 사용자가 참여 중인 프로젝트 목록 가져오기
    public List<Project> getProjectsByUser(Member member) {
        return projectRepository.findByMembers_Member(member);
    }

    // 프로젝트 상세
    public ProjectDetailChatDTO getProjectDetail(Long projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project Id:" + projectId));

        ProjectDetailChatDTO dto = new ProjectDetailChatDTO();
        dto.setProjectId(project.getId());
        dto.setProjectName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setTag1(project.getTag1());
        dto.setTag2(project.getTag2());
        dto.setTag3(project.getTag3());
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());
        dto.setStatus(project.getStatus());

        // 단체 채팅방 가져오기
        ChatRoom groupChatRoom = project.getChatRooms().stream()
                .filter(chatRoom -> chatRoom.getType() == ChatRoomType.GROUP)
                .findFirst()
                .orElse(null);

        if (groupChatRoom != null) {
            ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
            chatRoomDTO.setChatRoomId(groupChatRoom.getId());
            chatRoomDTO.setName(groupChatRoom.getName());
            chatRoomDTO.setType(groupChatRoom.getType());

            // 메시지 추가
            List<ChatMessageDTO> messages = groupChatRoom.getMessages().stream()
                    .map(message -> {
                        ChatMessageDTO messageDTO = new ChatMessageDTO();
                        messageDTO.setMessageId(message.getId());
                        messageDTO.setChatRoomId(groupChatRoom.getId());
                        messageDTO.setSenderName(message.getSender().getUsername());
                        messageDTO.setSenderProfile(message.getSender().getProfile());
                        messageDTO.setMessage(message.getMessage());
                        messageDTO.setSentAt(message.getSentAt());
                        return messageDTO;
                    })
                    .collect(Collectors.toList());

            chatRoomDTO.setMessages(messages);
            dto.setGroupChatRoom(chatRoomDTO);
        }

        return dto;
    }

    public Project createProject(ProjectCreateRequestDTO dto, Member creator) {

        Project project = new Project();
        project.setName(dto.getName()); // 프로젝트 이름
        project.setDescription(dto.getDescription()); // 프로젝트 설명
        project.setTag1(dto.getTag1()); // 카테고리1
        project.setTag2(dto.getTag2()); // 카테고리2
        project.setTag3(dto.getTag3()); // 카테고리3
        project.setStartDate(dto.getStartDate()); // 시작일
        project.setEndDate(dto.getEndDate()); // 마감일
        project.setStatus(ProjectStatus.BEFORE_START); // 프로젝트 상태(기본값 : 시작 전)
        project.setCreator(creator); // 프로젝트 생성자

        // 프로젝트 생성자를 프로젝트 멤버로 추가
        ProjectMember creatorMember = new ProjectMember();
        creatorMember.setProject(project);
        creatorMember.setMember(creator);
        creatorMember.setJoinedAt(LocalDateTime.now());
        project.getMembers().add(creatorMember);

        List<Member> invitedMembers = memberRepository.findAllById(dto.getInvitedMemberIds());

        for (Member member : invitedMembers) {

            ProjectMember projectMember = new ProjectMember();
            projectMember.setProject(project);
            projectMember.setMember(member);
            projectMember.setJoinedAt(LocalDateTime.now());
            project.getMembers().add(projectMember);
        }

        return projectRepository.save(project);
    }

    public ProjectUpdateResponseDTO getProjectForEdit(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("프로젝트가 존재하지 않습니다."));

        ProjectUpdateResponseDTO dto = new ProjectUpdateResponseDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setTag1(project.getTag1());
        dto.setTag2(project.getTag2());
        dto.setTag3(project.getTag3());
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());
        dto.setStatus(project.getStatus());

        // 멤버 정보 추가
        List<Long> memberIds = project.getMembers().stream()
                .map(pm -> pm.getMember().getId())
                .collect(Collectors.toList());
        List<String> memberNames = project.getMembers().stream()
                .map(pm -> pm.getMember().getUsername())
                .collect(Collectors.toList());

        dto.setMemberIds(memberIds);
        dto.setMemberNames(memberNames);

        return dto;
    }

    public void updateProject(Long projectId, ProjectUpdateRequestDTO projectUpdateRequestDTO) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("프로젝트가 존재하지 않습니다."));

        // 이름 수정
        if (projectUpdateRequestDTO.getName() != null) {
            project.setName(projectUpdateRequestDTO.getName());
        }
        // 태그 수정
        project.setTag1(projectUpdateRequestDTO.getTag1());
        project.setTag2(projectUpdateRequestDTO.getTag2());
        project.setTag3(projectUpdateRequestDTO.getTag3());

        // 시작일, 종료일 수정
        project.setStartDate(projectUpdateRequestDTO.getStartDate());
        project.setEndDate(projectUpdateRequestDTO.getEndDate());

        // 상태 수정
        project.setStatus(projectUpdateRequestDTO.getStatus());

        // 멤버 교체
        if (projectUpdateRequestDTO.getMemberIds() != null) {
            // 기존 멤버 삭제 (remove 사용)
            project.getMembers().clear();
            projectRepository.flush();

            // 새로운 멤버 추가
            List<Member> newMembers = memberRepository.findAllById(projectUpdateRequestDTO.getMemberIds());
            List<ProjectMember> updatedProjectMembers = newMembers.stream().map(member -> {
                ProjectMember projectMember = new ProjectMember();
                projectMember.setProject(project);
                projectMember.setMember(member);
                projectMember.setJoinedAt(LocalDateTime.now());
                return projectMember;
            }).collect(Collectors.toList());

            project.getMembers().addAll(updatedProjectMembers);
        }

        // 멤버 추가
        if (projectUpdateRequestDTO.getMemberIdsToAdd() != null && !projectUpdateRequestDTO.getMemberIdsToAdd().isEmpty()) {
            List<Member> membersToAdd = memberRepository.findAllById(projectUpdateRequestDTO.getMemberIdsToAdd());

            for (Member member : membersToAdd) {
                // 이미 멤버로 추가되어 있는지 확인
                boolean alreadyMember = project.getMembers().stream()
                        .anyMatch(pm -> pm.getMember().getId().equals(member.getId()));
                if (!alreadyMember) {
                    ProjectMember projectMember = new ProjectMember();
                    projectMember.setProject(project);
                    projectMember.setMember(member);
                    projectMember.setJoinedAt(LocalDateTime.now());
                    project.getMembers().add(projectMember);
                }
            }
        }

        // 멤버 삭제
        if (projectUpdateRequestDTO.getMemberIdsToRemove() != null && !projectUpdateRequestDTO.getMemberIdsToRemove().isEmpty()) {
            List<Long> idsToRemove = projectUpdateRequestDTO.getMemberIdsToRemove();

            project.getMembers().removeIf(pm -> idsToRemove.contains(pm.getMember().getId()));
        }

        projectRepository.save(project);
    }

    public void softDeleteProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("프로젝트가 존재하지 않습니다."));

        project.setDeleteStatus(DeleteStatus.DELETED);
        projectRepository.save(project);
    }


    public Project getProjectById(Long projectId) {
//        return projectRepository.findById(projectId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid project Id:" + projectId));
        return projectRepository.findByIdWithMembers(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project Id:" + projectId));
    }

    public boolean isUserMemberOfProject(Member user, Long projectId) {
        Project project = getProjectById(projectId);
        return project.getMembers().stream()
                .anyMatch(member -> member.getMember().getId().equals(user.getId()));
    }

    public ProjectDetailDTO getProjectDetailForView(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("프로젝트가 존재하지 않습니다."));

        ProjectDetailDTO dto = new ProjectDetailDTO();
        dto.setProjectId(project.getId());
        dto.setProjectName(project.getName());

        // Task 정보 추가
        List<TaskDTO> taskDTOList = project.getTasks().stream().map(task -> {
            TaskDTO taskDTO = new TaskDTO();
            taskDTO.setTaskId(task.getId());
            taskDTO.setTitle(task.getTitle());
            taskDTO.setStartDate(task.getStartDate());
            taskDTO.setEndDate(task.getEndDate());
            taskDTO.setStatus(task.getStatus());

            // 담당자 이름
            taskDTO.setAssignedMemberName(task.getAssignedMember() != null ? task.getAssignedMember().getUsername() : null);

            // 참여자 이름 목록
            List<String> participantNames = task.getParticipants().stream()
                    .map(taskMember -> taskMember.getMember().getUsername())
                    .collect(Collectors.toList());
            taskDTO.setParticipants(participantNames);

            return taskDTO;
        }).collect(Collectors.toList());

        dto.setTasks(taskDTOList);

        return dto;
    }
}
