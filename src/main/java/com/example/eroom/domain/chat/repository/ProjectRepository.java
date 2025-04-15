package com.example.eroom.domain.chat.repository;

import com.example.eroom.domain.entity.DeleteStatus;
import com.example.eroom.domain.entity.Project;
import com.example.eroom.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    // 현재 사용자가 속한 프로젝트 목록 가져오기
    //List<Project> findAllByMembersMember(Member member);
    List<Project> findByMembers_Member(Member member);

    List<Project> findByMembers_MemberAndDeleteStatus(Member member, DeleteStatus deleteStatus);

    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.members m WHERE p.id = :projectId")
    Optional<Project> findByIdWithMembers(@Param("projectId") Long projectId);

    Optional<Project> findById(Long id);

    // 프로젝트 이름으로 검색
    List<Project> findByNameContainingIgnoreCase(String name);

    // 현재 시각 + 24시간이 endDate와 일치하는 프로젝트 조회
    @Query("SELECT DISTINCT p FROM Project p WHERE p.endDate >= :startOfNextDay AND p.endDate < :endOfNextDay")
    List<Project> findProjectsEndingIn24Hours(
            @Param("startOfNextDay") LocalDateTime startOfNextDay,
            @Param("endOfNextDay") LocalDateTime endOfNextDay
    );
}
