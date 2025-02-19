package com.example.eroom.domain.chat.service;

import com.example.eroom.domain.chat.repository.NotificationRepository;
import com.example.eroom.domain.entity.Member;
import com.example.eroom.domain.entity.Notification;
import com.example.eroom.domain.entity.NotificationType;
import com.example.eroom.domain.entity.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    // 알림 생성
    public void createNotification(Member recipient, String message, NotificationType type, Long projectId) {

        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setMessage(message);
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setType(type);
        notification.setReferenceId(projectId);

        notificationRepository.save(notification);

        // 웹소켓으로 실시간 알림 전송
        messagingTemplate.convertAndSend("/notifications/" + recipient.getId(), message);
    }

    // 읽지 않은 알림 조회
    public List<Notification> getUnreadNotifications(Member member) {
        return notificationRepository.findByRecipientAndIsReadFalse(member);
    }

    // 알림 읽음 처리
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("알림이 존재하지 않습니다."));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
