package com.example.eroom.domain.chat.dto.response;

import com.example.eroom.domain.entity.ChatRoomType;
import lombok.Data;

import java.util.List;

@Data
public class ChatRoomDTO {

    private Long chatRoomId;
    private String name;
    private ChatRoomType type;
    private List<ChatMessageDTO> messages;
}
