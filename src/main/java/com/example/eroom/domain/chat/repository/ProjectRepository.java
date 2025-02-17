package com.example.eroom.domain.chat.repository;

import com.example.eroom.domain.entity.Project;
import com.example.eroom.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findAllByMembersUser(Member member);

    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.members m WHERE p.id = :projectId")
    Optional<Project> findByIdWithMembers(@Param("projectId") Long projectId);
}
