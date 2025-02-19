package com.example.eroom.domain.admin.controller;

import com.example.eroom.domain.admin.dto.MemberCountDTO;
import com.example.eroom.domain.admin.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/dashboard")
public class DashboardController {
    // 1. 누적 회원 수
    // 2. 신규 회원 수
    // 3. 결제 데이터
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) { this.dashboardService = dashboardService; }

    @GetMapping
    public ResponseEntity <List<MemberCountDTO>> getTotalMembers() {
        List<MemberCountDTO> totalMembers = dashboardService.getTotalMemberCount();
        return ResponseEntity.ok(totalMembers);
    }
}
