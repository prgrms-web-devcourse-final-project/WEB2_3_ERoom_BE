package com.example.eroom.domain.admin.controller;

import com.example.eroom.domain.admin.dto.response.DashboardDTO;
import com.example.eroom.domain.admin.dto.response.NewMemberCountDTO;
import com.example.eroom.domain.admin.dto.response.TotalMemberCountDTO;
import com.example.eroom.domain.admin.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) { this.dashboardService = dashboardService; }

    @GetMapping
    public ResponseEntity<List<DashboardDTO>> getDashboardData() {
        // 총 누적 회원 수
        List<TotalMemberCountDTO> totalMembers = dashboardService.getTotalMemberCount();
        // 신규 회원 수
        List<NewMemberCountDTO> newMembers = dashboardService.getNewMemberCount();

        DashboardDTO dashboardDTO = new DashboardDTO(totalMembers, newMembers);
        return ResponseEntity.ok(List.of(dashboardDTO));
    }
}
