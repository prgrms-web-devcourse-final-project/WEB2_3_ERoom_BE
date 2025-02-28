package com.example.eroom.domain.entity;

import com.example.eroom.domain.chat.converter.ColorInfoConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
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
    private List<ProjectSubCategory> projectSubCategories = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
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
    private List<ProjectMember> members = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Task> tasks = new ArrayList<>();

    @Convert(converter = ColorInfoConverter.class)
    @Column(columnDefinition = "TEXT")
    private ColorInfo colors;
}