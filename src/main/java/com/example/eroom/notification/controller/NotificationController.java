package com.example.eroom.notification.controller;

import com.example.eroom.entity.Member;
import com.example.eroom.entity.Notification;
import com.example.eroom.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/api/notifications/unread/{id}")
    //public List<Notification> getUnreadNotifications(@AuthenticationPrincipal Member member) {
    public List<Notification> getUnreadNotifications(@PathVariable Member member) {
            return notificationService.getUnreadNotifications(member);
    }

    @PostMapping("/api/notifications/mark-as-read/{id}")
    public ResponseEntity<String> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok("알림이 읽음 처리되었습니다.");
    }

    @MessageMapping("/sendNotification")  // 클라이언트 → 서버로 메시지 받을 때
    public void sendNotification(String message) {
        messagingTemplate.convertAndSend("/queue/notifications", message);
    }
}

