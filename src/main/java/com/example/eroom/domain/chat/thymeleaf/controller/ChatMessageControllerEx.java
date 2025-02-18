package com.example.eroom.domain.chat.thymeleaf.controller;

import com.example.eroom.domain.chat.dto.ChatMessageDTO;
import com.example.eroom.domain.chat.thymeleaf.service.ChatMessageServiceEx;
import com.example.eroom.domain.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatMessageControllerEx {

    private final ChatMessageServiceEx chatMessageServiceEx;
    private final SimpMessagingTemplate messagingTemplate;

//    @MessageMapping("/chat/send")
//    @SendTo("/topic/chatroom/{roomId}")
//    public ChatMessage sendMessage(ChatMessage message) {
//        return chatMessageService.saveMessage(message);
//    }

    @MessageMapping("/chat/send/ex")
    public void sendMessage(@Payload ChatMessageDTO chatMessageDTO) {  // 반환 타입을 void로 변경
        if (chatMessageDTO.getSenderUsername() == null) {
            throw new RuntimeException("SenderUsername is null!");
        }

        // DTO → Entity 변환 후 저장
        ChatMessage chatMessage = chatMessageServiceEx.convertToEntity(chatMessageDTO);
        chatMessageServiceEx.saveMessage(chatMessage);

        // SimpMessagingTemplate을 사용하여 메시지 전송
        messagingTemplate.convertAndSend(
                "/topic/chatroom/" + chatMessageDTO.getChatRoomId(),
                chatMessageDTO
        );
    }

}
