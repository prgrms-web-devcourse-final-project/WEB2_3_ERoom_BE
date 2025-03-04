package com.example.eroom.domain.report.controller;

import com.example.eroom.domain.chat.service.ChatRoomService;
import com.example.eroom.domain.entity.ChatRoom;
import com.example.eroom.domain.entity.DeleteStatus;
import com.example.eroom.domain.entity.Report;
import com.example.eroom.domain.report.dto.ReportDTO;
import com.example.eroom.domain.report.dto.ReportListDTO;
import com.example.eroom.domain.report.mapper.ReportStringToJson;
import com.example.eroom.domain.report.service.MeetingReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
    public ResponseEntity<ReportDTO> getMeetingSummary(
            @PathVariable Long chatRoomId,
            @PathVariable String title,
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {

        // 회의록 생성 요청
        String report = meetingReportService.generateMeetingSummary(chatRoomId, startTime, endTime);
        assert report != null;
        ReportDTO reportDTO = ReportStringToJson.parseReport(report);
        Report savedReport = Report.builder().
                chatRoom(chatRoomService.getChatRoomById(chatRoomId))
                .content(reportDTO.getContent())
                .title(title)
                .startDate(startTime)
                .endDate(endTime)
                .status(DeleteStatus.ACTIVE)
                .members(String.join(", ",reportDTO.getMembers()))
                .build();
        meetingReportService.saveReport(savedReport);
        return ResponseEntity.ok( reportDTO);
    }

    @PostMapping("/modify/{reportId}")
    public ResponseEntity<String> modifyReport(@PathVariable Long reportId, @RequestBody String content) {
        meetingReportService.updateReport(reportId, content);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list/{chatRoomId}")
    public List<ReportListDTO> getReports(@PathVariable Long chatRoomId) {
        ChatRoom chatRoom = chatRoomService.getChatRoomById(chatRoomId);
        List<Report> reports =  meetingReportService.getReportList(chatRoom);
        List<ReportListDTO> reportListDTOs = new ArrayList<>();
        for(Report report : reports) {
            ReportListDTO reportListDTO = ReportListDTO.builder()
                    .id(report.getId())
                    .members(Arrays.asList(report.getMembers().split(",")))
                    .content(report.getContent())
                    .title(report.getTitle())
                    .chatRoomId(chatRoomId)
                    .deleteStatus(report.getDeleteStatus())
                    .createdAt(report.getCreatedAt())
                    .startDate(report.getStartDate())
                    .endDate(report.getEndDate())
                    .build();
            reportListDTOs.add(reportListDTO);
        }
        return reportListDTOs;
    }

    @PostMapping("/delete/{reportId}")
    public ResponseEntity<String> deleteReport(@PathVariable Long reportId) {
        meetingReportService.softDeleteReport(reportId);
        return ResponseEntity.ok().build();
    }
}
