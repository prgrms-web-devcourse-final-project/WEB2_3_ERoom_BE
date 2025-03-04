package com.example.eroom.domain.chat.repository;

import com.example.eroom.domain.entity.Project;
import com.example.eroom.domain.entity.ProjectTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectTagRepository extends JpaRepository<ProjectTag, Long> {

    void deleteAllByProject(Project project);
}
