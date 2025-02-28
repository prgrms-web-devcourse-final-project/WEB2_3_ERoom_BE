package com.example.eroom.domain.report.repository;

import com.example.eroom.domain.entity.ChatRoom;
import com.example.eroom.domain.entity.DeleteStatus;
import com.example.eroom.domain.entity.Report;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    @Modifying
    @Query("UPDATE Report r SET r.content = :content WHERE r.id = :reportId")
    void updateReport(@Param("reportId") Long reportId, @Param("content") String content);

    @Query("SELECT r FROM Report r WHERE r.chatRoom = :chatRoom AND r.deleteStatus = :status ORDER BY r.createdAt ASC")
    List<Report> findActiveReports(@Param("chatRoom") ChatRoom chatRoom, @Param("status") DeleteStatus status);

    @Transactional
    @Modifying
    @Query("UPDATE Report r SET r.deleteStatus = :status WHERE r.id = :reportId")
    void softDeleteReport(@Param("reportId") Long reportId, @Param("status") DeleteStatus status);
}
