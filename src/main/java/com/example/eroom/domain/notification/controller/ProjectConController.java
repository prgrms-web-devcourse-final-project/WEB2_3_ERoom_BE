package com.example.eroom.domain.notification.controller;

import com.example.eroom.domain.entity.Member;
import com.example.eroom.domain.entity.Project;
import com.example.eroom.domain.notification.dto.ProjectCreateRequest;
import com.example.eroom.domain.notification.repository.MemberRepository2;
import com.example.eroom.domain.notification.service.ProjectService2;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectConController {
    private final ProjectService2 projectService;
    private final MemberRepository2 memberRepository;

    @PostMapping("/create2")
    public ResponseEntity<Project> createProject2(
            @RequestBody ProjectCreateRequest request) {

        Member creator = memberRepository.findByUsername(request.getCreator())
                .orElseThrow(() -> new RuntimeException("프로젝트 생성자 없음"));

        List<Member> members = memberRepository.findByUsernameIn(request.getMemberUsernames());

        Project project = projectService.createProject(request.getProjectName(), request.getDescription(), creator, members);

        return ResponseEntity.ok(project);
    }
}

