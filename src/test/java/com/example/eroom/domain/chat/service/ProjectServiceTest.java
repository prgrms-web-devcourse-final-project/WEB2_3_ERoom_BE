package com.example.eroom.domain.chat.service;

import com.example.eroom.domain.chat.dto.request.ProjectCreateRequestDTO;
import com.example.eroom.domain.chat.dto.request.ProjectUpdateRequestDTO;
import com.example.eroom.domain.chat.dto.response.ProjectDetailChatDTO;
import com.example.eroom.domain.chat.repository.MemberRepository;
import com.example.eroom.domain.chat.repository.ProjectRepository;
import com.example.eroom.domain.chat.repository.TaskRepository;
import com.example.eroom.domain.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @InjectMocks
    private ProjectService projectService;

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private NotificationService notificationService;
    @Mock
    private TaskRepository taskRepository;

    private Member creator;
    private Project project;
    private List<Member> invitedMembers;

    @BeforeEach
    void setUp() {

        // 프로젝트 생성자
        creator = new Member();
        creator.setId(1L);
        creator.setUsername("creator");
        creator.setEmail("creator@example.com");

        // 프로젝트 초대 멤버
        invitedMembers = new ArrayList<>();
        for (long i = 2; i <= 4; i++) {
            Member member = new Member();
            member.setId(i);
            member.setUsername("user" + i);
            member.setEmail("user" + i + "@example.com");
            invitedMembers.add(member);
        }

        // 프로젝트 설정
        project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setDescription("Test Description");
        project.setCreator(creator);
        project.setMembers(new ArrayList<>());

        // 생성자를 프로젝트 멤버로 추가
        ProjectMember creatorMember = new ProjectMember();
        creatorMember.setProject(project);
        creatorMember.setMember(creator);
        project.getMembers().add(creatorMember);
    }

    @Test
    void testCreateProject() {

        // Given
        ProjectCreateRequestDTO requestDTO = new ProjectCreateRequestDTO();
        requestDTO.setName("New Project");
        requestDTO.setDescription("New Description");
        requestDTO.setInvitedMemberIds(invitedMembers.stream().map(Member::getId).toList());

        when(memberRepository.findAllById(requestDTO.getInvitedMemberIds())).thenReturn(invitedMembers);
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> {
            Project savedProject = invocation.getArgument(0);
            savedProject.setId(1L);
            return savedProject;
        });

        // When
        Project createdProject = projectService.createProject(requestDTO, creator);

        // Then
        assertNotNull(createdProject);
        assertEquals("New Project", createdProject.getName());
        assertEquals(4, createdProject.getMembers().size()); // creator + 3 invited members

        verify(notificationService, times(3)).createNotification(any(), anyString(), eq(NotificationType.PROJECT_INVITE), anyLong());
    }

    @Test
    void testGetProjectDetail() {
        // Given
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        // When
        ProjectDetailChatDTO projectDetail = projectService.getProjectDetail(1L);

        // Then
        assertNotNull(projectDetail);
        assertEquals(project.getId(), projectDetail.getProjectId());
        assertEquals(project.getName(), projectDetail.getProjectName());
    }

    @Test
    void testUpdateProject() {
        // Given
        ProjectUpdateRequestDTO updateDTO = new ProjectUpdateRequestDTO();
        updateDTO.setName("Updated Project");
        updateDTO.setCategory("Updated Category");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        // When
        projectService.updateProject(1L, updateDTO, creator);

        // Then
        assertEquals("Updated Project", project.getName());
        assertEquals("Updated Category", project.getCategory());
    }

    @Test
    void testSoftDeleteProject() {
        // Given
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        // When
        projectService.softDeleteProject(1L, creator);

        // Then
        assertEquals(DeleteStatus.DELETED, project.getDeleteStatus());
    }

    @Test
    void testIsUserMemberOfProject() {
        // Given
        when(projectRepository.findByIdWithMembers(1L)).thenReturn(Optional.of(project));

        // When
        boolean isMember = projectService.isUserMemberOfProject(creator, 1L);

        // Then
        assertTrue(isMember);
    }

    @Test
    void testLeaveProject() {
        // Given
        Member leavingMember = invitedMembers.get(0);
        ProjectMember projectMember = new ProjectMember();
        projectMember.setProject(project);
        projectMember.setMember(leavingMember);
        project.getMembers().add(projectMember);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        // When
        projectService.leaveProject(1L, leavingMember);

        // Then
        assertFalse(project.getMembers().contains(projectMember));
    }

}