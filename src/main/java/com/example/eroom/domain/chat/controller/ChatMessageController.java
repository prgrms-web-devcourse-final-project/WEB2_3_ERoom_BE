package com.example.eroom.domain.chat.controller;

import com.example.eroom.domain.chat.dto.response.ChatMessageDTO;
import com.example.eroom.domain.chat.error.CustomException;
import com.example.eroom.domain.chat.error.ErrorCode;
import com.example.eroom.domain.chat.service.ChatMessageService;
import com.example.eroom.domain.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

//    @MessageMapping("/chat/send")
//    @SendTo("/topic/chatroom/{roomId}")
//    public ChatMessage sendMessage(ChatMessage message) {
//        return chatMessageService.saveMessage(message);
//    }

    @MessageMapping("/chat/send")
    public void sendMessage(@Payload ChatMessageDTO chatMessageDTO) {
        if (chatMessageDTO.getSenderName() == null) {
            throw new CustomException(ErrorCode.SENDER_NOT_FOUND);
        }

        System.out.println("chat senderName : " + chatMessageDTO.getSenderName());
        System.out.println("chat message : " + chatMessageDTO.getMessage());
        System.out.println("chat senderId : " + chatMessageDTO.getSenderId());

        // DTO → Entity 변환 후 저장
        ChatMessage chatMessage = chatMessageService.convertToEntity(chatMessageDTO);
        chatMessageService.saveMessage(chatMessage);

        // SimpMessagingTemplate을 사용하여 메시지 전송
        messagingTemplate.convertAndSend(
                "/topic/chatroom/" + chatMessageDTO.getChatRoomId(),
                chatMessageDTO
        );
    }
}
