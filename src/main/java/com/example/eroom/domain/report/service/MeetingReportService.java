package com.example.eroom.domain.report.service;

import com.example.eroom.domain.chat.repository.ChatMessageRepository;
import com.example.eroom.domain.entity.ChatMessage;
import com.example.eroom.domain.entity.ChatRoom;
import com.example.eroom.domain.entity.DeleteStatus;
import com.example.eroom.domain.entity.Report;
import com.example.eroom.domain.report.dto.Message;
import com.example.eroom.domain.report.dto.OpenAiRequest;
import com.example.eroom.domain.report.repository.ReportRepository;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MeetingReportService {

    private final ReportRepository reportRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final WebClient webClient;

    @Value("${openai.api.key}")
    private String openAiApiKey;

    public MeetingReportService(WebClient.Builder webClientBuilder, ChatMessageRepository chatMessageRepository, ReportRepository reportRepository) {
        this.webClient = webClientBuilder.baseUrl("https://api.openai.com").build();
        this.chatMessageRepository = chatMessageRepository;
        this.reportRepository = reportRepository;
    }

    public String generateMeetingSummary(Long chatRoomId, LocalDateTime startTime, LocalDateTime endTime) {
        List<ChatMessage> messages = chatMessageRepository.findByChatRoomIdAndSentAtBetweenOrderBySentAtAsc(chatRoomId, startTime, endTime);

        if (messages.isEmpty()) {
            return "해당 시간 동안 채팅 내역이 없습니다.";
        }

        // 메시지를 문자열로 변환
        String conversation = "채팅 시작 시간 : " + messages.get(0).getSentAt().toString() + " , ";
        conversation += messages.stream()
                .map(msg -> msg.getSender().getUsername() + " : " + msg.getMessage())
                .collect(Collectors.joining(" "));
        conversation += " , 채팅 끝나는 시간 : " + messages.get(messages.size() - 1).getSentAt().toString();

        if (conversation.length() > 3000) {
            log.info("conversation is too long, truncating...");
            conversation = conversation.substring(0, 3000) + "... (생략)";
        }

        OpenAiRequest request = new OpenAiRequest(
                "gpt-4",
                List.of(
                        new Message("system", "다음 채팅 기록을 기반으로 회의록을 작성해줘. 회의목 제목, 회의기간은 빼고 참여인원, 회의내용이 있어야하고 참여인원은 쉼표로 구분해. 또한 json형태로 보내는데, \"content\"와 \"members\"로 파싱하게 두개로 보내."),
                        new Message("user", conversation)
                ),
                0.7
        );

        // ChatGPT API 요청
        JsonNode response = webClient.post()
                .uri("/v1/chat/completions")
                .header("Authorization", "Bearer " + openAiApiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
        assert response != null;
        log.info(response.toString());
        return response.path("choices").get(0).path("message").path("content").asText();
    }

    public void saveReport(Report report) {
        reportRepository.save(report);
    }

    @Transactional
    public void updateReport(Long reportId, String content) {
        log.info("Updating reportId: {}, content: {}", reportId, content);
        reportRepository.updateReport(reportId, content);
    }

    public List<Report> getReportList(ChatRoom chatRoom) {
        return reportRepository.findActiveReports(chatRoom, DeleteStatus.ACTIVE);
    }

    public void softDeleteReport(Long ReportId){
        reportRepository.softDeleteReport(ReportId, DeleteStatus.DELETED);
    }
}
