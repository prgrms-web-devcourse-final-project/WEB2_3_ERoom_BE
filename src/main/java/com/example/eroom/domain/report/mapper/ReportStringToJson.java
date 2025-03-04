package com.example.eroom.domain.report.mapper;


import com.example.eroom.domain.entity.ChatRoom;
import com.example.eroom.domain.entity.DeleteStatus;
import com.example.eroom.domain.report.dto.ReportDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Arrays;

public class ReportStringToJson {
    public static ReportDTO parseReport(String reportJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        ReportDTO reportDTO = null;

        try {
            JsonNode rootNode = objectMapper.readTree(reportJson);
            reportDTO = new ReportDTO(rootNode.get("content").asText(), Arrays.asList(rootNode.get("members").asText().split(",")));
        } catch (Exception e) {
            e.printStackTrace(); // 예외 처리
        }

        return reportDTO;
    }
}
