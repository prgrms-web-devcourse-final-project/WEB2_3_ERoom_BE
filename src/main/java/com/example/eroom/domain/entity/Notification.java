package com.example.eroom.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Notification {

    @Builder
    Notification(Member recipient, String message, boolean isRead, LocalDateTime createdAt, NotificationType type, String referenceId, String referenceName){
        this.member = recipient;
        this.message = message;
        this.isRead = isRead;
        this.createdAt = createdAt;
        this.type = type;
        this.referenceId = referenceId;
        this.referenceName = referenceName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String message;
    @Setter
    private boolean isRead;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private NotificationType type; // PROJECT_INVITE, PROJECT_EXIT, MESSAGE_SEND...

    // 관련 엔티티의 ID만 저장하는 방식도 괜찮을듯
    private String referenceId;
    private String referenceName;
}