package com.example.eroom.domain.report.dto;

import com.example.eroom.domain.entity.DeleteStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ReportListDTO {
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
