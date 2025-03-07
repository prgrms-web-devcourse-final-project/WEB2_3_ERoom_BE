package com.example.eroom.domain.chat.service;

import com.example.eroom.domain.chat.dto.response.ChatMessageDTO;
import com.example.eroom.domain.chat.error.CustomException;
import com.example.eroom.domain.chat.error.ErrorCode;
import com.example.eroom.domain.chat.repository.ChatMessageRepository;
import com.example.eroom.domain.chat.repository.ChatRoomRepository;
import com.example.eroom.domain.chat.repository.MemberRepository;
import com.example.eroom.domain.chat.repository.NotificationRepository;
import com.example.eroom.domain.entity.ChatMessage;
import com.example.eroom.domain.entity.ChatRoom;
import com.example.eroom.domain.entity.Member;
import com.example.eroom.domain.entity.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    public ChatMessage saveMessage(ChatMessage message) {

        ChatMessage savedMessage = chatMessageRepository.save(message);

        List<Member> members = memberRepository.findMembersByChatRoomId(message.getChatRoom().getId());
        for (Member member : members) {
            if (!member.equals(message.getSender())) { // 자기 자신 제외
                notificationService.createNotification(member, "새로운 메시지가 도착했습니다: " + message.getMessage(), NotificationType.MESSAGE_SEND, message.getChatRoom().getId().toString() + " , " + message.getChatRoom().getProject().getId().toString(), message.getChatRoom().getName() + " , " + message.getChatRoom().getProject().getName());
            }
        }

        return savedMessage;
    }

    public List<ChatMessage> getMessagesByRoomId(Long roomId) {
        return chatMessageRepository.findByChatRoomIdOrderBySentAtAsc(roomId);
    }

    // DTO → Entity 변환 메서드
    public ChatMessage convertToEntity(ChatMessageDTO dto) {

        // ChatRoom 조회
        ChatRoom chatRoom = chatRoomRepository.findById(dto.getChatRoomId())
                .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));

        // Member 조회
        Member sender = memberRepository.findByUsername(dto.getSenderName());
        if (sender == null) {
            throw new CustomException(ErrorCode.SENDER_NOT_FOUND);
        }

        System.out.println("sender : " + sender);

        // ChatMessage 생성
        return ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .message(dto.getMessage())
                .unreadCount(0)
                .sentAt(dto.getSentAt())
                .build();
    }
}
