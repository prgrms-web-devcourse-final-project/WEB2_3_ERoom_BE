package com.example.eroom.domain.auth.scheduler;

import com.example.eroom.domain.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RefreshTokenCleanupTask {

    private final RefreshTokenRepository refreshTokenRepository;

    @Scheduled(cron = "0 0 3 * * ?") // 매일 새벽 3시에 실행
    public void deleteExpiredTokens() {
        refreshTokenRepository.deleteByExpiredAtBefore(LocalDateTime.now()); // 15일 지난 토큰 삭제
        System.out.println("3시에 만료된 RefreshToken을 일괄 삭제합니다.");
    }
}
