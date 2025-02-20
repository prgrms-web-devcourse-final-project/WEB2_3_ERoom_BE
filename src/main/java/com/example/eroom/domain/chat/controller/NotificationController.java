package com.example.eroom.domain.chat.controller;

import com.example.eroom.domain.chat.service.NotificationService;
import com.example.eroom.domain.chat.service.MemberService;
import com.example.eroom.domain.entity.Member;
import com.example.eroom.domain.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final MemberService memberService;

    // 현재 유저의 읽지 않은 알림 목록
    @ResponseBody
    @GetMapping("/unread/{memberId}")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@PathVariable Long memberId) {
        Member member = memberService.getMemberById(memberId);

        List<Notification> notifications = notificationService.getUnreadNotifications(member);

        return ResponseEntity.ok(notifications);
    }

    // 알림 읽음 처리
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.noContent().build();
    }
}