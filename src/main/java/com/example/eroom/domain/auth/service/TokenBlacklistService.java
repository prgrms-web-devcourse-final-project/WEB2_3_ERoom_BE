package com.example.eroom.domain.auth.service;

import com.example.eroom.domain.auth.repository.TokenBlacklistRepository;
import com.example.eroom.domain.entity.TokenBlacklist;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenBlacklistService {
    private final TokenBlacklistRepository tokenBlacklistRepository; // DB 연동

    public void blacklistAccessToken(String accessToken, long expirationTimeMillis) {
        // 만료 시간을 LocalDateTime으로 변환
        LocalDateTime expirationTime = LocalDateTime.now().plus(Duration.ofMillis(expirationTimeMillis));

        TokenBlacklist tokenBlacklist = new TokenBlacklist(accessToken, expirationTime);
        tokenBlacklistRepository.save(tokenBlacklist);

        log.info("AccessToken 블랙리스트 추가: {} ({})", accessToken, expirationTime);
    }

    public boolean isBlacklisted(String accessToken) {
        return tokenBlacklistRepository.findByAccessToken(accessToken).isPresent();
    }

    /*private final RedisTemplate<String, String> redisTemplate; // Redis 활용

    public void blacklistAccessToken(String accessToken, long expirationTimeMillis) {
        long expirationTimeSeconds = expirationTimeMillis / 1000; // Redis TTL은 초 단위
        redisTemplate.opsForValue().set(accessToken, "blacklisted", expirationTimeSeconds, TimeUnit.SECONDS);
        log.info("AccessToken 블랙리스트 추가: {} ({}초 후 삭제)", accessToken, expirationTimeSeconds);
    }

    public boolean isBlacklisted(String accessToken) {
        return redisTemplate.hasKey(accessToken);
    }*/
}

