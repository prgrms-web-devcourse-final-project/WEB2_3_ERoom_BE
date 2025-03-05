package com.example.eroom.domain.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final RedisTemplate<String, String> redisTemplate; // Redis 활용

    public void blacklistAccessToken(String accessToken, long expirationTimeMillis) {
        long expirationTimeSeconds = expirationTimeMillis / 1000; // Redis TTL은 초 단위
        redisTemplate.opsForValue().set(accessToken, "blacklisted", expirationTimeSeconds, TimeUnit.SECONDS);
        log.info("AccessToken 블랙리스트 추가: {} ({}초 후 삭제)", accessToken, expirationTimeSeconds);
    }

    public boolean isBlacklisted(String accessToken) {
        return redisTemplate.hasKey(accessToken);
    }
}

