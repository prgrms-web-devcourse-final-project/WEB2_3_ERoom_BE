package com.example.eroom.domain.auth.service;

import com.example.eroom.domain.auth.dto.request.OAuth2UserInfo;
import com.example.eroom.domain.auth.dto.request.SocialLoginRequest;
import com.example.eroom.domain.auth.dto.response.AuthResponse;
import com.example.eroom.domain.auth.repository.AuthMemberRepository;
import com.example.eroom.domain.auth.repository.RefreshTokenRepository;
import com.example.eroom.domain.auth.security.JwtTokenProvider;
import com.example.eroom.domain.auth.security.OAuth2TokenValidator;
import com.example.eroom.domain.entity.DeleteStatus;
import com.example.eroom.domain.entity.Member;
import com.example.eroom.domain.entity.MemberGrade;
import com.example.eroom.domain.entity.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthMemberRepository memberRepository;
    private final OAuth2TokenValidator oAuth2TokenValidator;
    private final RefreshTokenRepository refreshTokenRepository;

    // ✅ 1. 소셜 로그인 요청 처리
    public AuthResponse login(SocialLoginRequest request) {
        // (1) 소셜 토큰 검증
        OAuth2UserInfo userInfo = oAuth2TokenValidator.validateToken(request.getProvider(), request.getToken());
        if (userInfo == null || userInfo.getEmail() == null) {
            throw new IllegalArgumentException("OAuth2 토큰 검증 실패");
        }

        // (2) 이메일로 기존 회원 조회
        Optional<Member> existingMember = memberRepository.findByEmail(userInfo.getEmail());

        if (existingMember.isPresent()) {
            // ✅ 기존 회원 -> JWT 엑세스 & 리프레시 토큰 발급
            Member member = existingMember.get();

            String accessToken = jwtTokenProvider.createAccessToken(member.getEmail(), getRolesForMember(member));
            String refreshToken = jwtTokenProvider.createRefreshToken(member.getEmail());

            // ✅ 리프레시 토큰을 DB에 저장 (기존 토큰 덮어쓰기)
            refreshTokenRepository.save(new RefreshToken(member.getEmail(), refreshToken));

            return AuthResponse.ofExistingUser(member, accessToken, refreshToken);
        }

        // ✅ 신규 회원 -> 비회원 상태 정보 반환
        return AuthResponse.ofNewUser(userInfo);
    }


    // 신규 회원 생성
    private Member createNewMember(OAuth2UserInfo userInfo) {
        Member member = new Member();
        member.setEmail(userInfo.getEmail());
        member.setUsername(userInfo.getEmail()); // 또는 기본값 설정
        member.setMemberGrade(MemberGrade.DISABLE);  // 기본값: DISABLE
        member.setDeleteStatus(DeleteStatus.ACTIVE); // 기본값: ACTIVE
        return member;
    }

    // 회원의 역할을 가져오는 메서드
    private List<String> getRolesForMember(Member member) {
        // 예시: DISABLE과 ABLE은 'ROLE_USER' 역할을 부여하고, ADMIN은 'ROLE_ADMIN' 역할을 부여
        List<String> roles = new ArrayList<>();
        if (member.getMemberGrade() == MemberGrade.ADMIN) {
            roles.add("ROLE_ADMIN");
        } else {
            roles.add("ROLE_USER");
        }
        return roles;
    }
}


/*@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthMemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final OAuthTokenValidator oAuthTokenValidator;
    private final AmazonS3Service amazonS3Service;

    // ✅ 1. 로그인 기능
    public AuthResponse login(String token, String providerId, String provider) {
        // 1️⃣ 토큰 검증
        String email = oAuthTokenValidator.validateToken(token, provider);
        if (email == null) {
            throw new RuntimeException("유효하지 않은 소셜 로그인 토큰입니다.");
        }

        // 2️⃣ 회원 존재 여부 확인
        Optional<Member> existingMember = memberRepository.findByEmailAndProvider(email, provider);

        if (existingMember.isPresent()) {
            // 3️⃣ 기존 회원 → JWT 토큰 생성 후 반환
            Member member = existingMember.get();
            String jwtToken = jwtTokenProvider.createToken(member.getEmail(), member.getRole());
            return new AuthResponse(true, jwtToken, member);
        } else {
            // 4️⃣ 비회원 → OAuth 정보 반환
            return new AuthResponse(false, email, providerId, provider);
        }
    }

    // ✅ 2. 회원가입 기능
    public AuthResponse signup(String email, String providerId, String provider, String name, String organization, MultipartFile profileImage) {
        // 1️⃣ 프로필 이미지 S3 업로드
        String profileUrl = amazonS3Service.uploadFile(profileImage);

        // 2️⃣ 새로운 회원 저장
        Member member = Member.builder()
                .email(email)
                .provider(provider)
                .providerId(providerId)
                .username(name)
                .organization(organization)
                .profile(profileUrl)
                .memberGrade(MemberGrade.DISABLE)
                .deleteStatus(DeleteStatus.ACTIVE)
                .build();
        memberRepository.save(member);

        // 3️⃣ JWT 토큰 생성 후 반환
        String jwtToken = jwtTokenProvider.createToken(email, member.getRole());
        return new AuthResponse(true, jwtToken, member);
    }
}*/

