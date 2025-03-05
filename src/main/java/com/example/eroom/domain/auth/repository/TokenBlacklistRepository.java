package com.example.eroom.domain.auth.repository;

import com.example.eroom.domain.entity.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long> {
    Optional<TokenBlacklist> findByAccessToken(String accessToken);
    void deleteByAccessToken(String accessToken);

    List<TokenBlacklist> findAllByExpirationTimeBefore(LocalDateTime expirationTime);
}

