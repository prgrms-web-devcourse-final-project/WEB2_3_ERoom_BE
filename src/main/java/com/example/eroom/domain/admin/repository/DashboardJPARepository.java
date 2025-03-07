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

    @Query(value = """
        WITH RECURSIVE DateRange AS (
                    SELECT CURDATE() AS date
                    UNION ALL 
                    SELECT date - INTERVAL 1 DAY 
                    FROM DateRange
                    WHERE date > CURDATE() - INTERVAL 7 DAY
        )
        SELECT
               dr.date,
                COALESCE(COUNT(m.created_at), 0) AS newMembers
        FROM DateRange dr
        LEFT JOIN member m ON DATE(m.created_at) = dr.date
        GROUP BY dr.date
        ORDER BY dr.date;
        """, nativeQuery = true)
    List<Object[]> getNewMemberCount();

}
