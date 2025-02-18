package com.example.eroom.domain.chat.repository;

import com.example.eroom.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmailAndPassword(String email, String password);

    List<Member> findAllByIdNot(Long id);

    Member findByUsername(String username);
}
