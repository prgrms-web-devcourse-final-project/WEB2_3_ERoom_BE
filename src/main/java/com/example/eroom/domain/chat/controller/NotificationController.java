package com.example.eroom.domain.chat.controller;

import com.example.eroom.domain.chat.service.NotificationService;
import com.example.eroom.domain.entity.Member;
import com.example.eroom.domain.entity.Notification;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // 현재 유저의 읽지 않은 알림 목록
    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(HttpSession session) {
        Member currentMember = (Member) session.getAttribute("member");
        if (currentMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Notification> notifications = notificationService.getUnreadNotifications(currentMember);
        return ResponseEntity.ok(notifications);
    }

    // 알림 읽음 처리
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.noContent().build();
    }
}
