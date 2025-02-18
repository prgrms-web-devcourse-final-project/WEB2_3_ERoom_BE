package com.example.eroom.domain.admin.service;

import com.example.eroom.domain.admin.dto.MemberCountDTO;
import com.example.eroom.domain.admin.repository.DashboardJPARepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    private final DashboardJPARepository dashboardJPARepository;

    public DashboardService(DashboardJPARepository dashboardJPARepository) {
        this.dashboardJPARepository = dashboardJPARepository;
    }

    public List<MemberCountDTO> getTotalMemberCount() {
        List<MemberCountDTO> totalMembers = new ArrayList<>();

        // 4주 전 ~ 오늘까지 누적 회원 수 조회
        for( int i = 4; i >= 0; i-- ) {
            // 4주 전 ~ 오늘까지 누적 회원 수 조회
            List<Object[]> results = dashboardJPARepository.getTotalMemberCount(i);

            for( Object[] row : results ) {
                LocalDate startDate = LocalDate.parse(row[0].toString());
                Long members = ((Number) row[1]).longValue();

                totalMembers.add(new MemberCountDTO(startDate, members));
            }
        }
        return totalMembers;
    }
}
