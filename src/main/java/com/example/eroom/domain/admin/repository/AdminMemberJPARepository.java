package com.example.eroom.domain.admin.repository;

import com.example.eroom.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AdminMemberJPARepository extends JpaRepository<Member, Long> {
    @Query("SELECT DATE(m.createdAt) AS date, " +
            "COUNT(m) OVER (ORDER BY m.createdAt) AS totalMembers " +
            "FROM Member m " +
            "WHERE m.createdAt BETWEEN :startDate AND :endDate")
    List<Object[]> getTotalMemberCount(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
