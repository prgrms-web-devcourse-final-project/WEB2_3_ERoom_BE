package com.example.eroom.domain.chat.service;

import com.example.eroom.domain.chat.dto.request.ProjectCreateRequestDTO;
import com.example.eroom.domain.chat.dto.response.ChatMessageDTO;
import com.example.eroom.domain.chat.dto.response.ChatRoomDTO;
import com.example.eroom.domain.chat.dto.response.ProjectDetailDTO;
import com.example.eroom.domain.chat.repository.MemberRepository;
import com.example.eroom.domain.chat.repository.ProjectRepository;
import com.example.eroom.domain.entity.*;
import com.example.eroom.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
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
    public ProjectDetailDTO getProjectDetail(Long projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project Id:" + projectId));

        ProjectDetailDTO dto = new ProjectDetailDTO();
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

        sendProjectCreationNotification(project);

        return projectRepository.save(project);
    }

    private void sendProjectCreationNotification(Project project) {
        String message = "새 프로젝트 '" + project.getName() + "'에 초대되었습니다.";

        for (ProjectMember member : project.getMembers()) {
            notificationService.sendNotification(
                    member.getMember(),
                    message,
                    NotificationType.PROJECT_INVITE
                    //member.getFcmToken() // FCM 푸시 알림
            );
        }
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
}
