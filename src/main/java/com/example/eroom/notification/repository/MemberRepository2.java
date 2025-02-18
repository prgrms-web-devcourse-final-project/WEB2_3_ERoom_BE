package com.example.eroom.notification.repository;

import com.example.eroom.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository2 extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
    List<Member> findByUsernameIn(List<String> usernames);
}
