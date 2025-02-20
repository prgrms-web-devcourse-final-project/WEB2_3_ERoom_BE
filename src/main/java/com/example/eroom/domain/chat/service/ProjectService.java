package com.example.eroom.domain.chat.service;

import com.example.eroom.domain.chat.dto.request.ProjectCreateRequestDTO;
import com.example.eroom.domain.chat.dto.request.ProjectUpdateRequestDTO;
import com.example.eroom.domain.chat.dto.response.*;
import com.example.eroom.domain.chat.repository.MemberRepository;
import com.example.eroom.domain.chat.repository.NotificationRepository;
import com.example.eroom.domain.chat.repository.ProjectRepository;
import com.example.eroom.domain.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;



@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;

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
        dto.setCategory(project.getCategory());
        dto.setSubCategories1(project.getSubCategories1());
        dto.setSubCategories2(project.getSubCategories2());
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
        project.setCategory(dto.getCategory()); // 카테고리1
        project.setSubCategories1(dto.getSubCategories1()); // 카테고리2
        project.setSubCategories2(dto.getSubCategories2()); // 카테고리3
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

        // 프로젝트 저장
        Project savedProject = projectRepository.save(project);

        // 프로젝트 초대 알림 보내기
        for (Member member : invitedMembers) {
            String message = "새로운 프로젝트에 초대되었습니다: " + savedProject.getName();
            notificationService.createNotification(member, message, NotificationType.PROJECT_INVITE, savedProject.getId());// 알림생성, 저장, 알림 전송
        }

        return savedProject;
    }

    public ProjectUpdateResponseDTO getProjectForEdit(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("프로젝트가 존재하지 않습니다."));

        ProjectUpdateResponseDTO dto = new ProjectUpdateResponseDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setCategory(project.getCategory());
        dto.setSubCategories1(project.getSubCategories1());
        dto.setSubCategories2(project.getSubCategories2());
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
        project.setCategory(projectUpdateRequestDTO.getCategory());
        project.setSubCategories1(projectUpdateRequestDTO.getSubCategories1());
        project.setSubCategories2(projectUpdateRequestDTO.getSubCategories2());

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

    public void softDeleteProject(Long projectId, Member currentMember) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("프로젝트가 존재하지 않습니다."));

        // 프로젝트 생성자가 아닌 경우 예외 발생
        if (!project.getCreator().getId().equals(currentMember.getId())) {
            System.out.println("프로젝트 생성자만 삭제할 수 있습니다.");
            throw new IllegalStateException("프로젝트 생성자만 삭제할 수 있습니다.");
        }

        // 프로젝트에 속한 멤버가 생성자 혼자만 있는 경우에만 삭제 가능
        if (project.getMembers().size() > 1) {
            System.out.println("프로젝트에 다른 멤버가 없어야 삭제할 수 있습니다.");
            throw new IllegalStateException("프로젝트에 다른 멤버가 없어야 삭제할 수 있습니다.");
        }

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

    public void leaveProject(Long projectId, Member member) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("프로젝트가 존재하지 않습니다."));

        // 현재 사용자가 이 프로젝트의 멤버인지 확인
        boolean isMember = project.getMembers().stream()
                .anyMatch(pm -> pm.getMember().getId().equals(member.getId()));
        if (!isMember) {
            throw new IllegalArgumentException("해당 프로젝트의 멤버가 아닙니다.");
        }

        // 프로젝트 생성자는 나갈 수 없음 (프로젝트 소유권을 이전 하던가 해야함)
        if (project.getCreator().getId().equals(member.getId())) {
            throw new IllegalArgumentException("프로젝트 생성자는 나갈 수 없습니다.");
        }

        // 멤버 제거
        project.getMembers().removeIf(pm -> pm.getMember().getId().equals(member.getId()));
        projectRepository.save(project);
    }

    @Scheduled(cron = "0 */10 * * * ?") // 매 시간 정각(00:00, 01:00, 02:00...) 실행
    public void sendEndDateReminder() {
        LocalDateTime now = LocalDateTime.now().withNano(0);
        LocalDateTime startOfNextDay = now.plusHours(24).withNano(0); // 정확히 24시간 후
        LocalDateTime endOfNextDay = startOfNextDay.plusMinutes(10).withNano(0); // 1시간 범위

        List<Project> projects = projectRepository.findProjectsEndingIn24Hours(startOfNextDay, endOfNextDay);

        log.info("24시간 후 종료될 프로젝트 수: " + projects.size());

        for (Project project : projects) {
            log.info("알림 전송 대상 프로젝트: " + project.getName());
            for(ProjectMember projectMember : project.getMembers()){
                String message = "프로젝트가 마감 24시간 전입니다: " + project.getName();
                notificationService.createNotification(projectMember.getMember(), message, NotificationType.PROJECT_EXIT, project.getId());// 알림생성, 저장, 알림 전송
            }
        }
    }
}
