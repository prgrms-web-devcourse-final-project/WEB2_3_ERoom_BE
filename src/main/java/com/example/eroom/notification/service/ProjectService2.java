package com.example.eroom.notification.service;

import com.example.eroom.domain.chat.repository.ProjectRepository;
import com.example.eroom.entity.Member;
import com.example.eroom.entity.Project;
import com.example.eroom.entity.ProjectMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService2 {
    private final ProjectRepository projectRepository;
    private final NotificationService notificationService;

    public Project createProject(String projectName, String description, Member creator, List<Member> members) {
        Project project = new Project();
        project.setName(projectName);
        project.setDescription(description);
        project.setCreator(creator);
        project.setCreatedAt(LocalDateTime.now());

        Project savedProject = projectRepository.save(project);

        // 프로젝트 멤버들에게 알림 전송
        sendProjectCreationNotification(savedProject);

        return savedProject;
    }

    private void sendProjectCreationNotification(Project project) {
        String message = "새 프로젝트 '" + project.getName() + "'에 초대되었습니다.";

        for (ProjectMember member : project.getMembers()) {
            notificationService.sendNotification(
                    member.getMember(),
                    message,
                    "PROJECT_INVITE"
                    //member.getFcmToken() // FCM 푸시 알림
            );
        }
    }
}

