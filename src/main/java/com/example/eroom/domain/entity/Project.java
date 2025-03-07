package com.example.eroom.domain.entity;

import com.example.eroom.domain.chat.converter.ColorInfoConverter;
import com.example.eroom.domain.chat.error.CustomException;
import com.example.eroom.domain.chat.error.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "create_member_id", nullable = false)
    private Member creator; // 프로젝트 생성자

    private String name;
    private String description; // x
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category; // 하나의 카테고리(ex : 개발)

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ProjectSubCategory> projectSubCategories = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProjectTag> tags = new ArrayList<>(); // 서브 카테고리에서 선택한 태그들

    @Enumerated(EnumType.STRING)
    @Column(name = "delete_status", nullable = false)
    private DeleteStatus deleteStatus = DeleteStatus.ACTIVE; // ACTIVE, DELETED

    @Column(name = "start_date")
    private LocalDateTime startDate; // 프로젝트 시작 날짜

    @Column(name = "end_date")
    private LocalDateTime endDate; // 프로젝트 종료 날짜

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProjectStatus status = ProjectStatus.BEFORE_START; // 프로젝트 상태 기본값 START

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ChatRoom> chatRooms = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<ProjectMember> members = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Task> tasks = new ArrayList<>();

    @Convert(converter = ColorInfoConverter.class)
    @Column(columnDefinition = "TEXT")
    private ColorInfo colors;

    public void addProjectSubCategory(ProjectSubCategory projectSubCategory) {
        if (this.projectSubCategories == null) {
            this.projectSubCategories = new ArrayList<>();
        }
        this.projectSubCategories.add(projectSubCategory);
    }

    public void addProjectTag(ProjectTag tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        this.tags.add(tag);
    }

    public void addProjectMember(ProjectMember projectMember) {
        if (this.members == null) {
            this.members = new ArrayList<>();
        }
        this.members.add(projectMember);
    }

    public static Project createProject(
            String name,
            String description,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Member creator,
            Category category,
            ColorInfo colors
    ) {
        Project project = Project.builder()
                .name(name)
                .description(description)
                .deleteStatus(DeleteStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .startDate(startDate)
                .endDate(endDate)
                .status(ProjectStatus.BEFORE_START)
                .creator(creator)
                .category(category)
                .colors(colors != null ? colors : new ColorInfo("#FFFFFF", "#000000"))
                .build();

        // 프로젝트 생성자를 멤버로 추가
        project.addProjectMember(ProjectMember.createProjectMember(project, creator));

        return project;
    }

    public void addSubCategoryWithTags(SubCategory subCategory, List<Tag> tags) {
        ProjectSubCategory projectSubCategory = ProjectSubCategory.builder()
                .project(this)
                .subCategory(subCategory)
                .build();
        this.projectSubCategories.add(projectSubCategory);

        for (Tag tag : tags) {
            if (!tag.getSubCategory().getId().equals(subCategory.getId())) {
                throw new CustomException(ErrorCode.TAG_NOT_BELONG_TO_SUBCATEGORY);
            }

            ProjectTag projectTag = ProjectTag.builder()
                    .project(this)
                    .tag(tag)
                    .build();
            this.tags.add(projectTag);

            // 태그 사용 횟수 증가
            tag.incrementCount();
        }
    }

    // 상태 수정 메서드
    public void activateProject() {
        this.deleteStatus = DeleteStatus.ACTIVE;
    }
}