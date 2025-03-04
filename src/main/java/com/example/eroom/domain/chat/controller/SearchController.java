package com.example.eroom.domain.chat.controller;

import com.example.eroom.domain.chat.dto.response.MemberSearchResponseDTO;
import com.example.eroom.domain.chat.dto.response.ProjectSearchResponseDTO;
import com.example.eroom.domain.chat.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    // 멤버 검색
    @GetMapping("/members")
    public ResponseEntity<List<MemberSearchResponseDTO>> searchMembers(@RequestParam String name) {
        List<MemberSearchResponseDTO> members = searchService.searchMembersByName(name);
        return ResponseEntity.ok(members);
    }

    @GetMapping("/admin/members")
    public ResponseEntity<List<MemberSearchResponseDTO>> searchAdminMembers(@RequestParam String name) {
        List<MemberSearchResponseDTO> members = searchService.adminSearchMembersByName(name);
        return ResponseEntity.ok(members);
    }

    // 프로젝트 검색
    @GetMapping("/projects")
    public ResponseEntity<List<ProjectSearchResponseDTO>> searchProjects(@RequestParam String name) {
        List<ProjectSearchResponseDTO> projects = searchService.searchProjectsByName(name);
        return ResponseEntity.ok(projects);
    }
}
