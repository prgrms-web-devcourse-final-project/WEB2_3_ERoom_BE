package com.example.eroom.domain.chat.repository;

import com.example.eroom.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmailAndPassword(String email, String password);

    List<Member> findAllByIdNot(Long id);

    @Query("SELECT m FROM Member m " +
            "JOIN ChatRoomMember crm ON m.id = crm.member.id " +
            "WHERE crm.chatRoom.id = :chatRoomId")
    List<Member> findMembersByChatRoomId(@Param("chatRoomId") Long chatRoomId);

    Member findByUsername(String username);
}
