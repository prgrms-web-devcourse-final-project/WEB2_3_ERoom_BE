package com.example.eroom.domain.notification.service;

import com.example.eroom.domain.entity.Member;
import com.example.eroom.domain.entity.Notification;
import com.example.eroom.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate; // WebSocket 전송용

    public void sendNotification(Member member, String message, String type) {
        Notification notification = Notification.builder()
                .member(member)
                .message(message)
                .type(type)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);

        // 실시간 알림 전송 (WebSocket)
        messagingTemplate.convertAndSendToUser(member.getUsername(), "/queue/notifications", message);
    }

    public List<Notification> getUnreadNotifications(Member member) {
        return notificationRepository.findByMemberAndIsReadFalse(member);
    }

    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
