package com.example.eroom.domain.report.dto;

import com.example.eroom.domain.entity.DeleteStatus;
import com.example.eroom.domain.entity.Report;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ReportListDTO {

    @Builder
    ReportListDTO(Long id, String content, Long chatRoomId, LocalDateTime createdAt, LocalDateTime startDate, LocalDateTime endDate, String title, DeleteStatus deleteStatus, List<String> members) {
        this.id = id;
        this.content = content;
        this.chatRoomId = chatRoomId;
        this.createdAt = createdAt;
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.deleteStatus = deleteStatus;
        this.members = members;
    }
    private Long id;

    private String content;

    private Long chatRoomId;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private String title;

    private DeleteStatus deleteStatus; // ACTIVE, DELETED

    private List<String> members;
}
