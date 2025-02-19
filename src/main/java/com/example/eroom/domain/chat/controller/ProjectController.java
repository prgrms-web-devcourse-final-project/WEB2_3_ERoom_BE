package com.example.eroom.domain.chat.controller;

import com.example.eroom.domain.chat.dto.request.ProjectCreateRequestDTO;
import com.example.eroom.domain.chat.dto.request.ProjectUpdateRequestDTO;
import com.example.eroom.domain.chat.dto.request.TaskCreateRequestDTO;
import com.example.eroom.domain.chat.dto.response.*;
import com.example.eroom.domain.chat.service.ChatRoomService;
import com.example.eroom.domain.chat.service.ProjectService;
import com.example.eroom.domain.entity.Member;
import com.example.eroom.domain.entity.Project;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final ChatRoomService chatRoomService;

    // 프로젝트 목록 보기
    @GetMapping("/list")
    public ResponseEntity<List<ProjectListResponseDTO>> listProjects(HttpSession session) {

        Member currentMember = (Member) session.getAttribute("member");
        if (currentMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 현재 사용자의 프로젝트 목록 가져오기
        List<Project> projects = projectService.getProjectsByUser(currentMember);
        System.out.println("조회된 프로젝트 수: " + projects.size()); // 확인용 로그

        // DTO로 변환
        List<ProjectListResponseDTO> projectList = projects.stream().map(project ->
                new ProjectListResponseDTO(
                        project.getId(),
                        project.getName(),
                        project.getCreatedAt(),
                        project.getTag1(),
                        project.getTag2(),
                        project.getTag3(),
                        project.getStartDate(),
                        project.getEndDate(),
                        project.getStatus(),
                        project.getMembers().stream()
                                .map(pm -> pm.getMember().getUsername()) // 멤버 이름만 추출
                                .collect(Collectors.toList())
                )
        ).collect(Collectors.toList());

        return ResponseEntity.ok(projectList);
    }

    // 프로젝트의 채팅방을 불러오는 api
    @GetMapping("/{projectId}/chatroom")
    public ResponseEntity<ProjectDetailChatDTO> getProjectDetail(@PathVariable Long projectId, HttpSession session) {
        Member currentMember = (Member) session.getAttribute("member");
        if (currentMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ProjectDetailChatDTO projectDetail = projectService.getProjectDetail(projectId);
        return ResponseEntity.ok(projectDetail);
    }

    @PostMapping("/create")
    public ResponseEntity<ProjectResponseDTO> createProject(@RequestBody ProjectCreateRequestDTO projectCreateRequestDTO,
                                                            HttpSession session) {

        Member creator = (Member) session.getAttribute("member");
        if (creator == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 프로젝트 생성
        Project project = projectService.createProject(projectCreateRequestDTO, creator);

        // 단체 채팅방 생성
        chatRoomService.createGroupChatRoomForProject(project);

        ProjectResponseDTO responseDTO = new ProjectResponseDTO(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getCreator().getUsername(),
                project.getCreatedAt()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    // 기존 프로젝트 정보 조회 (수정용 폼)
    @GetMapping("/{projectId}/edit")
    public ResponseEntity<ProjectUpdateResponseDTO> getProjectForEdit(@PathVariable Long projectId, HttpSession session) {
        Member currentMember = (Member) session.getAttribute("member");
        if (currentMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ProjectUpdateResponseDTO projectUpdateResponse = projectService.getProjectForEdit(projectId);
        return ResponseEntity.ok(projectUpdateResponse);
    }

    // 프로젝트 업데이트
    @PatchMapping("/{projectId}/update")
    public ResponseEntity<Void> updateProject(@PathVariable Long projectId,
                                              @RequestBody ProjectUpdateRequestDTO projectUpdateRequestDTO,
                                              HttpSession session) {
        Member currentMember = (Member) session.getAttribute("member");
        if (currentMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        projectService.updateProject(projectId, projectUpdateRequestDTO);
        return ResponseEntity.noContent().build();
    }

    // 프로젝트 삭제 (softDelete)
    @PatchMapping("/{projectId}/delete")
    public ResponseEntity<Void> softDeleteProject(@PathVariable Long projectId, HttpSession session) {
        Member currentMember = (Member) session.getAttribute("member");
        if (currentMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        projectService.softDeleteProject(projectId);
        return ResponseEntity.noContent().build();
    }


    // 프로젝트 상세보기 (채팅방 X, Task 정보 포함)
    @GetMapping("/{projectId}/detail")
    public ResponseEntity<ProjectDetailDTO> getProjectDetailForView(@PathVariable Long projectId, HttpSession session) {
        Member currentMember = (Member) session.getAttribute("member");
        if (currentMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ProjectDetailDTO projectDetail = projectService.getProjectDetailForView(projectId);
        return ResponseEntity.ok(projectDetail);
    }

    // 프로젝트 나가기
    @DeleteMapping("/{projectId}/leave")
    public ResponseEntity<Void> leaveProject(@PathVariable Long projectId, HttpSession session) {
        Member currentMember = (Member) session.getAttribute("member");
        if (currentMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        projectService.leaveProject(projectId, currentMember);
        return ResponseEntity.noContent().build();
    }

}
