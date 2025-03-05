package com.example.eroom.domain.auth.scheduler;

import com.example.eroom.domain.auth.repository.RefreshTokenRepository;
import com.example.eroom.domain.auth.repository.TokenBlacklistRepository;
import com.example.eroom.domain.entity.TokenBlacklist;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RefreshTokenCleanupTask {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenBlacklistRepository tokenBlacklistRepository;

    @Scheduled(cron = "0 0 3 * * ?") // 매일 새벽 3시에 실행
    public void deleteExpiredTokens() {
        refreshTokenRepository.deleteByExpiredAtBefore(LocalDateTime.now()); // 15일 지난 토큰 삭제
        System.out.println("3시에 만료된 RefreshToken을 일괄 삭제합니다.");
    }

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    public void cleanUpExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        List<TokenBlacklist> expiredTokens = tokenBlacklistRepository.findAllByExpirationTimeBefore(now);

        if (!expiredTokens.isEmpty()) {
            tokenBlacklistRepository.deleteAll(expiredTokens);
            System.out.println("3시에 BlackList에 있는 토큰을 일괄 삭제합니다. :" + expiredTokens.size());
        }
    }
}
