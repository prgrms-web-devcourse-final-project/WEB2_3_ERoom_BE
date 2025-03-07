package com.example.eroom.domain.auth.security;


import com.example.eroom.domain.auth.repository.AuthMemberRepository;
import com.example.eroom.domain.auth.repository.RefreshTokenRepository;
import com.example.eroom.domain.entity.Member;
import com.example.eroom.domain.entity.MemberGrade;
import com.example.eroom.domain.entity.RefreshToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final AuthMemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.secret}")
    private String secretKey;

    private SecretKey key;

    private final long accessTokenValidity = 1000L * 60 * 60; // 1시간
    private final long refreshTokenValidity = 1000L * 60 * 60 * 24 * 15; // 15일

    @PostConstruct
    protected void init() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 엑세스 토큰 생성
    public String createAccessToken(String email, MemberGrade memberGrade) {
        return createToken(email, memberGrade, accessTokenValidity);
    }

    // 리프레시 토큰 생성
    public String createRefreshToken(String email) {
        return createToken(email, null, refreshTokenValidity);
    }

    public String createAndStoreRefreshToken(String email) {
        String newRefreshToken = createRefreshToken(email);
        LocalDateTime expirationTime = LocalDateTime.now().plus(Duration.ofMillis(refreshTokenValidity));

        RefreshToken refreshToken = refreshTokenRepository.findByEmail(email)
                .orElse(new RefreshToken(email, newRefreshToken, expirationTime)); // 기존 토큰 없으면 생성

        refreshToken.updateToken(newRefreshToken, expirationTime); // 만료 시간 업데이트
        refreshTokenRepository.save(refreshToken);

        return newRefreshToken;
    }

    // JWT 생성
    private String createToken(String email, MemberGrade memberGrade, long validityInMilliseconds) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", email);  // "sub"은 subject를 의미
        //System.out.println(memberGrade.name());
        String role;
        if (memberGrade == MemberGrade.ADMIN) {
            role = "ROLE_ADMIN";
        } else if (memberGrade == MemberGrade.ABLE || memberGrade == MemberGrade.DISABLE) {
            role = "ROLE_USER";
        } else {
            role = "ROLE_GUEST";  // DISABLE은 임시적으로 제한된 권한을 부여
        }
        claims.put("role", role);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // WT 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 토큰에서 사용자 정보 가져오기
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        String email = claims.getSubject();
        String role = claims.get("role", String.class);

        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("JWT에서 이메일을 추출할 수 없습니다.");
        }

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        List<GrantedAuthority> authorities = new ArrayList<>();
        if (role != null) {
            authorities.add(new SimpleGrantedAuthority(role)); // role 값 적용
        }

        return new UsernamePasswordAuthenticationToken(member, token, authorities);
    }

    // JWT에서 Claims 추출
    public Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // JWT 만료 시간 가져오기
    public long getExpirationTime(String token) {
        Claims claims = parseClaims(token);
        Date expiration = claims.getExpiration();
        return expiration.getTime() - System.currentTimeMillis(); // 현재 시간과의 차이 반환
    }
}




