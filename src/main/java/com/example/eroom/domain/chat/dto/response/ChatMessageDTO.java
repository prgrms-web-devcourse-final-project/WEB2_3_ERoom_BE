package com.example.eroom.domain.chat.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageDTO {

    private Long messageId;
    private Long chatRoomId;
    private Long senderId;
    private String senderName;
    private String senderProfile;
    private String message;
    private LocalDateTime sentAt = LocalDateTime.now();
}