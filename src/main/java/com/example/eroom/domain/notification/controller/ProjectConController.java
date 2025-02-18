package com.example.eroom.domain.notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectConController {
    //private final MemberRepository2 memberRepository;

    /*@PostMapping("/create2")
    public ResponseEntity<Project> createProject2(
            @RequestBody ProjectCreateRequest request) {

        Member creator = memberRepository.findByUsername(request.getCreator())
                .orElseThrow(() -> new RuntimeException("프로젝트 생성자 없음"));

        List<Member> members = memberRepository.findByUsernameIn(request.getMemberUsernames());

        return ResponseEntity.ok(project);
    }*/
}

