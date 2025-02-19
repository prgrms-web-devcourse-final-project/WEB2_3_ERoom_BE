package com.example.eroom.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member recipient; // 알림을 받는 유저

    private String message;
    private boolean isRead;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private NotificationType type; // PROJECT_INVITE, PROJECT_EXIT, MESSAGE_SEND...

    // 관련 엔티티의 ID만 저장하는 방식도 괜찮을듯
    private Long referenceId;
}