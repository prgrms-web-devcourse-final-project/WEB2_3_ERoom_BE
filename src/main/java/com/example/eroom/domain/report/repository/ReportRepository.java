package com.example.eroom.domain.report.repository;

import com.example.eroom.domain.entity.ChatRoom;
import com.example.eroom.domain.entity.Report;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    @Modifying
    @Query("UPDATE Report r SET r.content =: content WHERE r.id=:reportId")
    void updateReport(@Param("reportId") Long reportId, @Param("content") String content);

    List<Report> findAllByChatRoomOrderByCreatedAtAsc(ChatRoom chatRoom);
}
