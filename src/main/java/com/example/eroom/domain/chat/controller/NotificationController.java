package com.example.eroom.domain.chat.controller;

import com.example.eroom.domain.chat.service.NotificationService;
import com.example.eroom.domain.chat.dto.NotificationDTO;
import com.example.eroom.domain.chat.thymeleaf.service.MemberService;
import com.example.eroom.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final MemberService memberService;

    @GetMapping("/notifications/{memberId}")
    public String getNotifications(
            //@SessionAttribute(name = "memberId", required = false) Long memberId,
            @PathVariable Long memberId,
            Model model) {

        /*if (memberId == null) {
            return "redirect:/login"; // 로그인 안 된 경우 로그인 페이지로 이동
        }*/

        Member member = memberService.getMemberById(memberId);
        List<NotificationDTO> notifications = notificationService.getNotificationsByMemberId(memberId);

        model.addAttribute("notifications", notifications);
        model.addAttribute("currentMember", member);

        return "notification"; // templates/notification.html 렌더링
    }
}
