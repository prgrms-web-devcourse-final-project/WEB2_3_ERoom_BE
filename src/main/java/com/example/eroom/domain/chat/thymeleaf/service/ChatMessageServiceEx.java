package com.example.eroom.domain.chat.thymeleaf.service;

import com.example.eroom.domain.chat.dto.ChatMessageDTO;
import com.example.eroom.domain.chat.repository.ChatMessageRepository;
import com.example.eroom.domain.chat.repository.ChatRoomRepository;
import com.example.eroom.domain.chat.repository.MemberRepository;
import com.example.eroom.domain.chat.repository.NotificationRepository;
import com.example.eroom.domain.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceEx {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatMessage saveMessage(ChatMessage message) {
        createChatNotification(message);

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
        Member sender = memberRepository.findByUsername(dto.getSenderUsername());
        if (sender == null) {
            throw new RuntimeException("Sender not found: " + dto.getSenderUsername());
        }

        chatMessage.setChatRoom(chatRoom);
        chatMessage.setSender(sender);
        chatMessage.setMessage(dto.getMessage());
        chatMessage.setUnreadCount(dto.getUnreadCount());
        chatMessage.setSentAt(dto.getSentAt());

        return chatMessage;
    }

    public void createChatNotification(ChatMessage message) {
        ChatRoom chatRoom = message.getChatRoom();
        List<Member> members = memberRepository.findMembersByChatRoomId(chatRoom.getId());

        for (Member member : members) {
            if (!member.equals(message.getSender())) { // 자기 자신 제외
                Notification notification = new Notification();
                notification.setMessage("새로운 메시지가 도착했습니다: " + message.getMessage());
                notification.setNotificationType(NotificationType.MESSAGE_SEND);
                notification.setRead(false);
                notification.setMember(member);
                notificationRepository.save(notification);

                // 웹소켓을 통해 실시간 알림 전송
                messagingTemplate.convertAndSend("/topic/notifications/" + member.getId(), notification);
            }
        }
    }
}
