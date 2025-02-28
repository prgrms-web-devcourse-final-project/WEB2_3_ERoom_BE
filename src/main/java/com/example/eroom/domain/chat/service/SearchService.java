package com.example.eroom.domain.chat.service;

import com.example.eroom.domain.chat.dto.response.MemberSearchResponseDTO;
import com.example.eroom.domain.chat.dto.response.ProjectSearchResponseDTO;
import com.example.eroom.domain.chat.repository.MemberRepository;
import com.example.eroom.domain.chat.repository.ProjectRepository;
import com.example.eroom.domain.entity.DeleteStatus;
import com.example.eroom.domain.entity.Member;
import com.example.eroom.domain.entity.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;

    // 멤버 검색
    public List<MemberSearchResponseDTO> searchMembersByName(String name) {
        List<Member> members = memberRepository.findByUsernameContainingIgnoreCaseAndDeleteStatus(name, DeleteStatus.ACTIVE);
        return members.stream()
                .map(member -> new MemberSearchResponseDTO(
                        member.getId(),
                        member.getUsername(),
                        member.getEmail(),
                        member.getProfile(),
                        member.getDeleteStatus()
                ))
                .collect(Collectors.toList());
    }

    // 관리자 멤버 검색
    public List<MemberSearchResponseDTO> adminSearchMembersByName(String name) {
        List<Member> members = memberRepository.findByUsernameContainingIgnoreCase(name);
        return members.stream()
                .map(member -> new MemberSearchResponseDTO(
                        member.getId(),
                        member.getUsername(),
                        member.getEmail(),
                        member.getProfile(),
                        member.getDeleteStatus()
                ))
                .collect(Collectors.toList());
    }

    // 프로젝트 검색
    public List<ProjectSearchResponseDTO> searchProjectsByName(String name) {
        List<Project> projects = projectRepository.findByNameContainingIgnoreCase(name);
        return projects.stream()
                .map(project -> new ProjectSearchResponseDTO(
                        project.getId(),
                        project.getName(),
                        project.getCreator().getUsername(),
                        project.getStatus(),
                        project.getDeleteStatus()
                ))
                .collect(Collectors.toList());
    }
}
