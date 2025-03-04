package com.example.eroom.domain.chat.repository;

import com.example.eroom.domain.entity.Project;
import com.example.eroom.domain.entity.ProjectSubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectSubCategoryRepository extends JpaRepository<ProjectSubCategory, Long> {

    void deleteAllByProject(Project project);
}
