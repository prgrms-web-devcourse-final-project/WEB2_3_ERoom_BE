package com.example.eroom.domain.chat.service;

import com.example.eroom.domain.chat.repository.NotificationRepository;
import com.example.eroom.domain.entity.Member;
import com.example.eroom.domain.entity.Notification;
import com.example.eroom.domain.entity.NotificationType;
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
    public Notification createNotification(Member recipient, String message, NotificationType type, String referenceIds, String referenceNames) {

        Notification notification = Notification.builder()
                .recipient(recipient)
                .message(message)
                .type(type)
                .referenceId(referenceIds)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .referenceName(referenceNames)
                .build();

        notificationRepository.save(notification);

        // 웹소켓으로 실시간 알림 전송
        messagingTemplate.convertAndSend("/notifications/" + recipient.getId(), notification);
        return notification;
    }

    // 읽지 않은 알림 조회
    public List<Notification> getUnreadNotifications(Member member) {
        return notificationRepository.findByMemberAndIsReadFalseOrderByCreatedAtDesc(member);
    }

    // 알림 읽음 처리
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("알림이 존재하지 않습니다."));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    // 맴버 아이디에 해당되는 알림들 읽음 처리
    public String markNotificationsAsRead(Long memberId) {
        try {
            int updatedCount = notificationRepository.markAllAsReadByMember(memberId);
            return updatedCount + "개의 알림이 읽음 처리되었습니다.";
        } catch (Exception e) {
            e.printStackTrace();  // 콘솔에 오류 출력
            return "알림 읽음 처리 중 오류가 발생했습니다.";
        }
    }
}
