package com.example.eroom.domain.admin.repository;

import com.example.eroom.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface DashboardJPARepository extends JpaRepository <Member, Long> {
    @Query(value = """
        SELECT DATE_FORMAT(DATE_SUB(NOW(), INTERVAL ?1 WEEK), '%Y-%m-%d') AS startDate,
               (SELECT COUNT(*) FROM member WHERE created_at <= DATE_SUB(NOW(), INTERVAL ?1 WEEK)) AS totalMembers  
        """, nativeQuery = true)
    List<Object[]> getTotalMemberCount(int weeksAgo);


}
