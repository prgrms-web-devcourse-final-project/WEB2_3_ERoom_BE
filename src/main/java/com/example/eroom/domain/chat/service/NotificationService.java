package com.example.eroom.domain.chat.service;

import com.example.eroom.domain.chat.thymeleaf.service.MemberService;
import com.example.eroom.domain.entity.Member;
import com.example.eroom.domain.entity.Notification;
import com.example.eroom.domain.chat.dto.NotificationDTO;
import com.example.eroom.domain.chat.repository.NotificationRepository;
import com.example.eroom.domain.entity.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final MemberService memberService;

    // 특정 멤버에게 알림 저장
    public void createNotification(Long memberId, String message, NotificationType type) {
        Member member = memberService.getMemberById(memberId);
        Notification notification = new Notification();
        notification.setMember(member);
        notification.setMessage(message);
        notification.setNotificationType(type);
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(notification);
    }

    // 특정 멤버의 알림 조회 (DTO 변환하여 반환)
    public List<NotificationDTO> getNotificationsByMemberId(Long memberId) {
        List<Notification> notifications = notificationRepository.findByMemberIdOrderByCreatedAtDesc(memberId);
        return notifications.stream()
                .map(NotificationDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // 특정 알림 읽음 처리
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
