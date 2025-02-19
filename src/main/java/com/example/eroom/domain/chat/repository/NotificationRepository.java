package com.example.eroom.domain.chat.repository;

import com.example.eroom.domain.entity.Member;
import com.example.eroom.domain.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientAndIsReadFalse(Member recipient);
}
