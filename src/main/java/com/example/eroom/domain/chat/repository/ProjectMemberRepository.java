package com.example.eroom.domain.chat.repository;

import com.example.eroom.domain.entity.Member;
import com.example.eroom.domain.entity.Project;
import com.example.eroom.domain.entity.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

    // 현재 유저를 제외한 해당 프로젝트 참가자 가져오기
    @Query("SELECT pm.member FROM ProjectMember pm WHERE pm.project = :project AND pm.member != :currentMember")
    List<Member> findMembersByProjectAndMemberNot(@Param("project") Project project, @Param("currentMember") Member currentMember);
}
