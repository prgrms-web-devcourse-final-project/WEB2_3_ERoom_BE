package com.example.eroom.notification.controller;

import com.example.eroom.entity.Member;
import com.example.eroom.entity.Project;
import com.example.eroom.notification.repository.MemberRepository;
import com.example.eroom.notification.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final MemberRepository memberRepository;

    @PostMapping("/create")
    public ResponseEntity<Project> createProject(
            @RequestParam String projectName,
            @RequestParam String description,
            @RequestParam String ownerUsername,
            @RequestParam List<String> memberUsernames) {

        Member creator = memberRepository.findByUsername(ownerUsername)
                .orElseThrow(() -> new RuntimeException("프로젝트 생성자 없음"));

        List<Member> members = memberRepository.findByUsernameIn(memberUsernames);

        Project project = projectService.createProject(projectName, description, creator, members);

        return ResponseEntity.ok(project);
    }
}

