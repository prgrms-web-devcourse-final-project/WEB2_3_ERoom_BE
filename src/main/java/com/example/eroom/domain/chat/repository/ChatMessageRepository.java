package com.example.eroom.domain.chat.repository;

import com.example.eroom.domain.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // openAI(Chat-gpt API)로 회의록 생성하는 쿼리, 나중에 분리 필요할 수도, 하지만 chat 디렉토리에서도 사용 중
    List<ChatMessage> findByChatRoomIdOrderBySentAtAsc(Long chatRoomId);
    
    // openAI(Chat-gpt API)로 회의록 생성하는 쿼리, 나중에 분리 필요할 수도
    List<ChatMessage> findByChatRoomIdAndSentAtBetweenOrderBySentAtAsc(Long chatRoomId, LocalDateTime startTime, LocalDateTime endTime);
}
