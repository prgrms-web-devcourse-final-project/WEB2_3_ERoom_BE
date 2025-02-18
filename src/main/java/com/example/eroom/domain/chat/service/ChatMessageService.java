package com.example.eroom.domain.chat.service;

import com.example.eroom.domain.chat.dto.response.ChatMessageDTO;
import com.example.eroom.domain.chat.repository.ChatMessageRepository;
import com.example.eroom.domain.chat.repository.ChatRoomRepository;
import com.example.eroom.domain.chat.repository.MemberRepository;
import com.example.eroom.entity.ChatMessage;
import com.example.eroom.entity.ChatRoom;
import com.example.eroom.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    public ChatMessage saveMessage(ChatMessage message) {
        return chatMessageRepository.save(message);
    }

    public List<ChatMessage> getMessagesByRoomId(Long roomId) {
        return chatMessageRepository.findByChatRoomIdOrderBySentAtAsc(roomId);
    }

    // DTO → Entity 변환 메서드
    public ChatMessage convertToEntity(ChatMessageDTO dto) {
        ChatMessage chatMessage = new ChatMessage();

        // ChatRoom 조회
        ChatRoom chatRoom = chatRoomRepository.findById(dto.getChatRoomId())
                .orElseThrow(() -> new RuntimeException("ChatRoom not found"));

        // User 조회
        Member sender = memberRepository.findByUsername(dto.getSenderName());
        if (sender == null) {
            throw new RuntimeException("Sender not found: " + dto.getSenderName());
        }

        chatMessage.setChatRoom(chatRoom);
        chatMessage.setSender(sender);
        chatMessage.setMessage(dto.getMessage());
        chatMessage.setUnreadCount(0);
        chatMessage.setSentAt(dto.getSentAt());

        return chatMessage;
    }
}
