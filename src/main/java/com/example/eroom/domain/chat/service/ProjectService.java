package com.example.eroom.domain.chat.service;

import com.example.eroom.domain.chat.repository.ProjectMemberRepository;
import com.example.eroom.domain.chat.repository.ProjectRepository;
import com.example.eroom.domain.chat.repository.MemberRepository;
import com.example.eroom.domain.entity.Project;
import com.example.eroom.domain.entity.Member;
import com.example.eroom.domain.entity.ProjectMember;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final ProjectMemberRepository projectMemberRepository;

    public ProjectService(ProjectRepository projectRepository, MemberRepository memberRepository, ProjectMemberRepository projectMemberRepository) {
        this.projectRepository = projectRepository;
        this.memberRepository = memberRepository;
        this.projectMemberRepository = projectMemberRepository;
    }

    public List<Project> getProjectsByUser(Member member) {
        return projectRepository.findAllByMembersMember(member);
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
