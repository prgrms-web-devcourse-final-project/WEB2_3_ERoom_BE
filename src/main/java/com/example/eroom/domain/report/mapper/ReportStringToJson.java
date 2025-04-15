package com.example.eroom.domain.report.mapper;


import com.example.eroom.domain.report.dto.ReportDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;

public class ReportStringToJson {
    public static ReportDTO parseReport(String reportJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        ReportDTO reportDTO = null;

        try {
            JsonNode rootNode = objectMapper.readTree(reportJson);
            String finalContent = rootNode.has("content")
                    ? rootNode.get("content").asText()
                    : rootNode.has("contents")
                    ? rootNode.get("contents").asText()
                    : "";
            String finalMembers = rootNode.has("members")
                    ? rootNode.get("members").asText()
                    : "";
            reportDTO = new ReportDTO(finalContent, Arrays.asList(finalMembers.split(",")));
        } catch (Exception e) {
            e.printStackTrace(); // 예외 처리
        }

        return reportDTO;
    }
}
