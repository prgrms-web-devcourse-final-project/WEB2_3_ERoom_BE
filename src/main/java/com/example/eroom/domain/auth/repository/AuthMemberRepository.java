package com.example.eroom.domain.auth.repository;

import com.example.eroom.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthMemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
}
