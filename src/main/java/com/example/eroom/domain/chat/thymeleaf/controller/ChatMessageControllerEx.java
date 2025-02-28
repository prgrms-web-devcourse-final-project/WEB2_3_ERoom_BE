package com.example.eroom.domain.chat.thymeleaf.controller;

import com.example.eroom.domain.chat.dto.ChatMessageExDTO;
import com.example.eroom.domain.chat.service.NotificationService;
import com.example.eroom.domain.chat.thymeleaf.service.ChatMessageServiceEx;
import com.example.eroom.domain.chat.service.MemberService;
import com.example.eroom.domain.entity.ChatMessage;
import com.example.eroom.domain.entity.Member;
import com.example.eroom.domain.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatMessageControllerEx {

    private final ChatMessageServiceEx chatMessageServiceEx;
    private final SimpMessagingTemplate messagingTemplate;
    private final MemberService memberService;
    private final NotificationService notificationService;

//    @MessageMapping("/chat/send")
//    @SendTo("/topic/chatroom/{roomId}")
//    public ChatMessage sendMessage(ChatMessage message) {
//        return chatMessageService.saveMessage(message);
//    }

    @MessageMapping("/chat/send/ex")
    public void sendMessage(@Payload ChatMessageExDTO chatMessageExDTO) {  // 반환 타입을 void로 변경
        if (chatMessageExDTO.getSenderName() == null) {
            throw new RuntimeException("SenderUsername is null!");
        }

        // DTO → Entity 변환 후 저장
        ChatMessage chatMessage = chatMessageServiceEx.convertToEntity(chatMessageExDTO);
        chatMessageServiceEx.saveMessage(chatMessage);

        // SimpMessagingTemplate을 사용하여 메시지 전송
        messagingTemplate.convertAndSend(
                "/topic/chatroom/" + chatMessageExDTO.getChatRoomId(),
                chatMessageExDTO
        );
    }

    @GetMapping("/notifications/{memberId}")
    public String Notifications(@PathVariable Long memberId, Model model) {
        Member member = memberService.getMemberById(memberId);

        List<Notification> notifications = notificationService.getUnreadNotifications(member);

        model.addAttribute("notifications", notifications);
        model.addAttribute("currentMember", member);

        return "notification";
    }

}