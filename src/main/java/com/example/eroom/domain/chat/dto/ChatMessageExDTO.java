package com.example.eroom.domain.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageExDTO {
    private Long chatRoomId;
    private String senderName; // sender의 username
    private Long senderId;
    private String message;
    private Integer unreadCount;
    private LocalDateTime sentAt = LocalDateTime.now();
}