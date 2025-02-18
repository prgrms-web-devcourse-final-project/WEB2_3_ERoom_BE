package com.example.eroom.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
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
    @JoinColumn(name = "member_id", nullable = false)
    private Member creator; // 프로젝트 생성자

    private String name;
    private String description; // x
    private LocalDateTime createdAt = LocalDateTime.now();
    // 카테고리
    private String tag1;
    private String tag2;
    private String tag3;

    @Enumerated(EnumType.STRING)
    private DeleteStatus deleteStatus = DeleteStatus.ACTIVE; // ACTIVE, DELETED

    @Column(name = "start_date")
    private LocalDateTime startDate; // 프로젝트 시작 날짜

    @Column(name = "end_date")
    private LocalDateTime endDate; // 프로젝트 종료 날짜

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status = ProjectStatus.BEFORE_START; // 프로젝트 상태 기본값 START

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ChatRoom> chatRooms = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ProjectMember> members = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Task> tasks = new ArrayList<>();
}
