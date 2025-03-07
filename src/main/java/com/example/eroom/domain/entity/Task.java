package com.example.eroom.domain.entity;

import com.example.eroom.domain.chat.converter.ColorInfoConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description; // x
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatus status = TaskStatus.BEFORE_START; // 테스크 상태 기본값 BEFORE_START

    @Enumerated(EnumType.STRING)
    @Column(name = "delete_status", nullable = false)
    private DeleteStatus deleteStatus = DeleteStatus.ACTIVE; // ACTIVE, DELETED

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "assigned_member_id")
    private Member assignedMember; // 담당자

//    // 참여자 (여러 명 가능) -> 추후 도입
//    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
//    @Builder.Default
//    private List<TaskMember> participants = new ArrayList<>();

    @Convert(converter = ColorInfoConverter.class)
    @Column(columnDefinition = "TEXT")
    private ColorInfo colors;

    // Task 수정 메서드
    public void updateTask(String title, LocalDateTime startDate, LocalDateTime endDate, TaskStatus status) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    // Task 삭제 (Soft Delete)
    public void deleteTask() {
        this.deleteStatus = DeleteStatus.DELETED;
    }

    // 담당자 변경 메서드
    public void updateAssignedMember(Member assignedMember) {
        this.assignedMember = assignedMember;
    }

//    // 참여자 변경 메서드 -> 추후 도입
//    public void updateParticipants(List<TaskMember> newParticipants) {
//        // null 체크 추가
//        if (this.participants == null) {
//            this.participants = new ArrayList<>();
//        }
//        this.participants.clear();
//        this.participants.addAll(newParticipants);
//    }

    // 상태 수정 메서드
    public void activateTask() {
        this.deleteStatus = DeleteStatus.ACTIVE;
    }
}
