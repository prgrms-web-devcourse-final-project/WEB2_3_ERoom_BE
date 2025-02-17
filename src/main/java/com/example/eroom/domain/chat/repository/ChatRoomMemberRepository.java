package com.example.eroom.domain.chat.repository;

import com.example.eroom.entity.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
}
