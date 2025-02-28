package com.example.eroom.domain.report.controller;

import com.example.eroom.domain.chat.service.ChatRoomService;
import com.example.eroom.domain.entity.ChatRoom;
import com.example.eroom.domain.entity.DeleteStatus;
import com.example.eroom.domain.entity.Report;
import com.example.eroom.domain.report.service.MeetingReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/report")
public class ReportController {
    private final ChatRoomService chatRoomService;
    private final MeetingReportService meetingReportService;

    public ReportController(MeetingReportService meetingReportService, ChatRoomService chatRoomService) {
        this.meetingReportService = meetingReportService;
        this.chatRoomService = chatRoomService;
    }

    // 요청시 ISO 8601형식으로 보내야 함!
    // 예를 들어 2025-02-21T10:00:00 이러한 형식
    // 대문자 T 절대 빼먹으로 안됨
    @GetMapping("/create/{chatRoomId}/{title}")
    public ResponseEntity<String> getMeetingSummary(
            @PathVariable Long chatRoomId,
            @PathVariable String title,
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {

        // 회의록 생성 요청
        String report = meetingReportService.generateMeetingSummary(chatRoomId, startTime, endTime).toString();
        log.info(report);
        Report reportDto = new Report();
        reportDto.setChatRoom(chatRoomService.getChatRoomById(chatRoomId));
        reportDto.setContent(report);
        reportDto.setTitle(title);
        reportDto.setStartDate(startTime);
        reportDto.setEndDate(endTime);
        reportDto.setDeleteStatus(DeleteStatus.ACTIVE);
        meetingReportService.saveReport(reportDto);
        return ResponseEntity.ok( report);
    }

    @PostMapping("/modify/{reportId}")
    public ResponseEntity<String> modifyReport(@PathVariable Long reportId, @RequestBody String content) {
        meetingReportService.updateReport(reportId, content);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list/{chatRoomId}")
    public List<Report> getReports(@PathVariable Long chatRoomId) {
        ChatRoom chatRoom = chatRoomService.getChatRoomById(chatRoomId);
        return meetingReportService.getReportList(chatRoom);
    }

    @PostMapping("/delete/{reportId}")
    public ResponseEntity<String> deleteReport(@PathVariable Long reportId) {
        meetingReportService.softDeleteReport(reportId);
        return ResponseEntity.ok().build();
    }
}
