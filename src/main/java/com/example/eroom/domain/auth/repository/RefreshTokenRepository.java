package com.example.eroom.domain.auth.repository;


import com.example.eroom.domain.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String email);

    Optional<RefreshToken> findByEmail(String email);

    void deleteByEmail(String email);

    void deleteByExpiredAtBefore(LocalDateTime now);
}
