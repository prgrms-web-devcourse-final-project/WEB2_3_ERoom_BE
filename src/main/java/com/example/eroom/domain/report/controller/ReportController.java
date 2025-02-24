package com.example.eroom.domain.report.controller;

import com.example.eroom.domain.report.service.MeetingReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequestMapping("/api/report")
public class ReportController {
    private final MeetingReportService meetingReportService;

    public ReportController(MeetingReportService meetingReportService) {
        this.meetingReportService = meetingReportService;
    }

    // 요청시 ISO 8601형식으로 보내야 함!
    // 예를 들어 2025-02-21T10:00:00 이러한 형식
    // 대문자 T 절대 빼먹으로 안됨
    @GetMapping("/{chatRoomId}")
    public ResponseEntity<String> getMeetingSummary(
            @PathVariable Long chatRoomId,
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime){

        // 회의록 생성 요청
        String report = meetingReportService.generateMeetingSummary(chatRoomId, startTime, endTime);
        log.info(report);
        return ResponseEntity.ok(report);
    }
}
