package com.example.eroom.domain.admin.dto.response;

import com.example.eroom.domain.entity.Project;
import com.example.eroom.domain.entity.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminProjectDTO {
    private Long projectId;
    private String projectName;
    private String assignedEmail;
    private ProjectStatus projectStatus;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private String category;
    private List<String> subCategories1;
    private List<String> subCategories2;

    // Project 엔티티를 매개변수로 받는 생성자
    public AdminProjectDTO(Project project) {
        this.projectId = project.getId();
        this.projectName = project.getName();
        this.assignedEmail = project.getCreator().getEmail();
        this.projectStatus = project.getStatus();
        this.startDate = project.getStartDate();
        this.endDate = project.getEndDate();

        this.category = project.getCategory();
        this.subCategories1 = project.getSubCategories1();
        this.subCategories2 = project.getSubCategories2();
    }
}
