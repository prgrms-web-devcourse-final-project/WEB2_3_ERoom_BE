package com.example.eroom.domain.chat.repository;

import com.example.eroom.domain.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // 특정 멤버의 모든 알림 조회
    List<Notification> findByMemberId(Long memberId);

    // 특정 멤버의 읽지 않은 알림 개수 조회
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.member.id = :memberId AND n.isRead = false")
    Long countUnreadNotifications(@Param("memberId") Long memberId);

    List<Notification> findByMemberIdOrderByCreatedAtDesc(Long memberId);
}
