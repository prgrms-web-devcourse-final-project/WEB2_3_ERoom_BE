package com.example.eroom.domain.chat.controller;

import com.example.eroom.domain.chat.dto.request.ProjectCreateRequestDTO;
import com.example.eroom.domain.chat.dto.request.ProjectUpdateRequestDTO;
import com.example.eroom.domain.chat.dto.response.*;
import com.example.eroom.domain.chat.service.ChatRoomService;
import com.example.eroom.domain.chat.service.MemberService;
import com.example.eroom.domain.chat.service.ProjectService;
import com.example.eroom.domain.entity.*;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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
        List<ProjectListResponseDTO> projectList = projects.stream().map(project -> {
            // Task 상태별 개수
            long completedTasks = project.getTasks().stream()
                    .filter(task -> task.getStatus() == TaskStatus.COMPLETED)
                    .count();

            long inProgressOrBeforeStartTasks = project.getTasks().stream()
                    .filter(task -> task.getStatus() == TaskStatus.IN_PROGRESS || task.getStatus() == TaskStatus.BEFORE_START)
                    .count();

            // 진행률 계산 (완료된 작업이 없으면 0%)
            double progressRate = (completedTasks == 0) ? 0.0
                    : (double) completedTasks / (completedTasks + inProgressOrBeforeStartTasks) * 100;

            // members 리스트를 MemberDTO로 변환
            List<MemberDTO> memberDTOs = project.getMembers().stream()
                    .map(pm -> new MemberDTO(pm.getMember().getId(), pm.getMember().getUsername(), pm.getMember().getProfile()))
                    .collect(Collectors.toList());

            List<SubCategoryDetail> subCategoryDetails = project.getProjectSubCategories().stream()
                    .map(projectSubCategory -> {
                        List<TagDetail> tagDetails = project.getTags().stream()
                                .filter(projectTag -> projectTag.getTag().getSubCategory().getId().equals(projectSubCategory.getSubCategory().getId())) // ✅ 올바르게 수정
                                .map(projectTag -> new TagDetail(
                                        projectTag.getTag().getId(),
                                        projectTag.getTag().getName()
                                ))
                                .collect(Collectors.toList());

                        return new SubCategoryDetail(
                                projectSubCategory.getSubCategory().getId(),
                                projectSubCategory.getSubCategory().getName(),
                                tagDetails
                        );
                    })
                    .collect(Collectors.toList());

            // 그룹 채팅방 찾기
            Long groupChatRoomId = project.getChatRooms().stream()
                    .filter(chatRoom -> chatRoom.getType() == ChatRoomType.GROUP)
                    .map(ChatRoom::getId)
                    .findFirst()
                    .orElse(null);

            return new ProjectListResponseDTO(
                    project.getId(),
                    project.getName(),
                    project.getCreatedAt(),
                    project.getCategory().getName(),
                    subCategoryDetails,
                    project.getStartDate(),
                    project.getEndDate(),
                    project.getStatus(),
                    memberDTOs,
                    groupChatRoomId,
                    progressRate,
                    project.getColors(),
                    project.getCreator().getId()
            );
        }).collect(Collectors.toList());

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

/*    @RequestMapping(value = "/{projectId}/update", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> handleOptions() {
        return ResponseEntity.ok().build();
    }*/

    // 프로젝트 업데이트
    @PatchMapping("/{projectId}/update")
    public ResponseEntity<Void> updateProject(@PathVariable Long projectId,
                                              @RequestBody ProjectUpdateRequestDTO projectUpdateRequestDTO,
                                              HttpSession session) {
        Member currentMember = (Member) session.getAttribute("member");
        if (currentMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        projectService.updateProject(projectId, projectUpdateRequestDTO, currentMember);
        return ResponseEntity.ok().build();
    }

    // 프로젝트 삭제 (softDelete)
    @DeleteMapping("/{projectId}/delete")
    public ResponseEntity<Void> softDeleteProject(@PathVariable Long projectId, HttpSession session) {
        Member currentMember = (Member) session.getAttribute("member");
        if (currentMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            projectService.softDeleteProject(projectId, currentMember);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
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
