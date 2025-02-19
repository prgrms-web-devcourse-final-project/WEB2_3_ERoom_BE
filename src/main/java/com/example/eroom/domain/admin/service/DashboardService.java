package com.example.eroom.domain.admin.service;

import com.example.eroom.domain.admin.dto.response.NewMemberCountDTO;
import com.example.eroom.domain.admin.dto.response.TotalMemberCountDTO;
import com.example.eroom.domain.admin.repository.DashboardJPARepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardService {
    private final DashboardJPARepository dashboardJPARepository;

    public DashboardService(DashboardJPARepository dashboardJPARepository) {
        this.dashboardJPARepository = dashboardJPARepository;
    }

    // 1. 총 누적 회원 수
    public List<TotalMemberCountDTO> getTotalMemberCount() {
        List<TotalMemberCountDTO> totalMembers = new ArrayList<>();

        // 4주 전 ~ 오늘까지 누적 회원 수 조회
        for( int i = 4; i >= 0; i-- ) {
            List<Object[]> results = dashboardJPARepository.getTotalMemberCount(i);

            for( Object[] row : results ) {
                LocalDate startDate = LocalDate.parse(row[0].toString());
                Long members = ((Number) row[1]).longValue();

                totalMembers.add(new TotalMemberCountDTO(startDate, members));
            }
        }
        return totalMembers;
    }

    // 2. 신규 회원 수
    public List<NewMemberCountDTO> getNewMemberCount() {
        List<NewMemberCountDTO> newMembers = new ArrayList<>();
        // 7일 전 ~ 오늘까지 신규 회원 수 조회
        List<Object[]> results = dashboardJPARepository.getNewMemberCount();

        for( Object[] row : results ) {
            LocalDate date = LocalDate.parse(row[0].toString());
            Long members = ((Number) row[1]).longValue();

            newMembers.add(new NewMemberCountDTO(date, members));
        }
        return newMembers;

    }
}
