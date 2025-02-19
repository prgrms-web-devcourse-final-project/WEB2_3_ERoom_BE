package com.example.eroom.domain.notification.repository;

import com.example.eroom.domain.entity.Member;
import com.example.eroom.domain.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByMemberAndIsReadFalse(Member member);
}
