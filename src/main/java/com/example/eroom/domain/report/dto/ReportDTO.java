package com.example.eroom.domain.report.dto;

import com.example.eroom.domain.entity.ChatRoom;
import com.example.eroom.domain.entity.DeleteStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ReportDTO {
    private String content;
    private List<String> members;
}
