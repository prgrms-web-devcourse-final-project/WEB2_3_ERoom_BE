package com.example.eroom.domain.auth.repository;

import com.example.eroom.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AuthMemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    @Modifying
    @Query("UPDATE Member m SET m.username = :username, m.organization = :organization WHERE m.id = :id")
    void updateMemberProfile(@Param("id") Long id, @Param("username") String username, @Param("organization") String organization);

    @Modifying
    @Query("UPDATE Member m SET m.profile = :profileUrl WHERE m.id = :id")
    void updateMemberProfileImage(@Param("id") Long id, @Param("profileUrl") String profileUrl);

}
