package com.example.eroom.domain.chat.thymeleaf.service;

import com.example.eroom.domain.chat.repository.ProjectMemberRepository;
import com.example.eroom.domain.chat.repository.ProjectRepository;
import com.example.eroom.domain.chat.repository.MemberRepository;
import com.example.eroom.domain.chat.service.NotificationService;
import com.example.eroom.domain.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceEx {

    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    public List<Project> getProjectsByUser(Member member) {
        return projectRepository.findByMembers_Member(member);
    }

    public void createProject(Project project, Member creator, List<Long> invitedUserIds) {
        project.setCreator(creator);
        projectRepository.save(project);

        // 프로젝트 생성자 추가
        ProjectMember creatorMember = new ProjectMember();
        creatorMember.setProject(project);
        creatorMember.setMember(creator);
        projectMemberRepository.save(creatorMember);

        // 초대한 유저들 추가
        List<Member> invitedMembers = memberRepository.findAllById(invitedUserIds);
        for (Member invitedMember : invitedMembers) {
            ProjectMember member = new ProjectMember();
            member.setProject(project);
            member.setMember(invitedMember);
            projectMemberRepository.save(member);
            String message = "새로운 프로젝트에 초대되었습니다: " + project.getName();
            Notification notification = notificationService.createNotification(invitedMember, message, NotificationType.PROJECT_INVITE, project.getId());
            messagingTemplate.convertAndSend("/topic/notifications/"+member.getId(), notification);
        }
    }

    public Project getProjectById(Long projectId) {
//        return projectRepository.findById(projectId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid project Id:" + projectId));
        return projectRepository.findByIdWithMembers(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project Id:" + projectId));
    }

    // 현재 유저를 제외한 프로젝트 참가자 목록 가져오기
    public List<Member> getProjectParticipantsExceptCurrentMember(Project project, Member currentMember) {
        return projectMemberRepository.findMembersByProjectAndMemberNot(project, currentMember);
    }

    public boolean isUserMemberOfProject(Member user, Long projectId) {
        Project project = getProjectById(projectId);
        return project.getMembers().stream()
                .anyMatch(member -> member.getMember().getId().equals(user.getId()));
    }
}
