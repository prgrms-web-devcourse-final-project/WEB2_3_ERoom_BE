package com.example.eroom.domain.report.service;

import com.example.eroom.domain.chat.repository.ChatMessageRepository;
import com.example.eroom.domain.entity.ChatMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeetingReportService {

    private final ChatMessageRepository chatMessageRepository;
    private final WebClient webClient;

    @Value("${openai.api.key}")
    private String openAiApiKey;

    public MeetingReportService(WebClient.Builder webClientBuilder, ChatMessageRepository chatMessageRepository) {
        this.webClient = webClientBuilder.baseUrl("https://api.openai.com").build();
        this.chatMessageRepository = chatMessageRepository;
    }

    public String generateMeetingSummary(Long chatRoomId, LocalDateTime startTime, LocalDateTime endTime) {
        List<ChatMessage> messages = chatMessageRepository.findByChatRoomIdAndSentAtBetweenOrderBySentAtAsc(chatRoomId, startTime, endTime);

        if (messages.isEmpty()) {
            return "해당 시간 동안 채팅 내역이 없습니다.";
        }

        // 메시지를 문자열로 변환
        String conversation = messages.stream()
                .map(msg -> "시간: " + msg.getSentAt() + " 그리고 송신자: " + msg.getSender().getUsername() + " 메세지 내용: " + msg.getMessage())
                .collect(Collectors.joining("\n"));

        // ChatGPT API 요청
        return webClient.post()
                .uri("/v1/chat/completions")
                .header("Authorization", "Bearer " + openAiApiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                            {
                                "model": "gpt-4",
                                "messages": [{"role": "system", "content": "다음 채팅 기록을 기반으로 회의록을 작성해줘."},
                                             {"role": "user", "content": "%s"}],
                                "temperature": 0.7
                            }
                        """.formatted(conversation))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
