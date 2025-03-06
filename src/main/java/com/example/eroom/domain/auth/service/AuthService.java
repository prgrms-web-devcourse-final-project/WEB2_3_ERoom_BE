package com.example.eroom.domain.auth.service;

import com.example.eroom.domain.auth.dto.request.OAuth2UserInfoDTO;
import com.example.eroom.domain.auth.dto.request.SignupRequestDTO;
import com.example.eroom.domain.auth.dto.request.SocialLoginRequestDTO;
import com.example.eroom.domain.auth.dto.response.AuthResponseDTO;
import com.example.eroom.domain.auth.repository.AuthMemberRepository;
import com.example.eroom.domain.auth.repository.RefreshTokenRepository;
import com.example.eroom.domain.auth.security.JwtTokenProvider;
import com.example.eroom.domain.auth.security.OAuth2TokenValidator;
import com.example.eroom.domain.entity.DeleteStatus;
import com.example.eroom.domain.entity.Member;
import com.example.eroom.domain.entity.MemberGrade;
import com.example.eroom.domain.entity.RefreshToken;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final AmazonS3Service amazonS3Service;
    private final PasswordEncoder passwordEncoder;

    // 소셜 로그인 요청 처리
    public AuthResponseDTO login(SocialLoginRequestDTO request) {
        // 소셜 토큰 검증
        OAuth2UserInfoDTO userInfo = oAuth2TokenValidator.validateToken(request.getProvider(), request.getToken());
        if (userInfo == null || userInfo.getEmail() == null) {
            throw new IllegalArgumentException("OAuth2 토큰 검증 실패");
        }

        // 이메일로 기존 회원 조회
        Optional<Member> existingMember = memberRepository.findByEmail(userInfo.getEmail());

        if (existingMember.isPresent()) {
            // 기존 회원 -> JWT 엑세스 & 리프레시 토큰 발급
            Member member = existingMember.get();

            String accessToken = jwtTokenProvider.createAccessToken(member.getEmail(), getRolesForMember(member));
            String refreshToken = jwtTokenProvider.createAndStoreRefreshToken(member.getEmail());

            // 리프레시 토큰을 DB에 저장 (기존 토큰 덮어쓰기)
            LocalDateTime expirationTime = LocalDateTime.now().plusDays(15);
            refreshTokenRepository.save(new RefreshToken(member.getEmail(), refreshToken, expirationTime));

            return AuthResponseDTO.ofExistingUser(member, accessToken, refreshToken);
        }

        // 신규 회원 -> 비회원 상태 정보 반환
        return AuthResponseDTO.ofNewUser(userInfo);
    }


    // 신규 회원 생성
    private Member createNewMember(OAuth2UserInfoDTO userInfo) {
        Member member = new Member();
        member.setEmail(userInfo.getEmail());
        member.setUsername(userInfo.getEmail()); // 또는 기본값 설정
        member.setMemberGrade(MemberGrade.DISABLE);  // 기본값: DISABLE
        member.setDeleteStatus(DeleteStatus.ACTIVE); // 기본값: ACTIVE
        return member;
    }

    // 회원의 역할을 가져오는 메서드
    private List<String> getRolesForMember(Member member) {
        List<String> roles = new ArrayList<>();
        if (member.getMemberGrade() == MemberGrade.ADMIN) {
            roles.add("ROLE_ADMIN");
        } else {
            roles.add("ROLE_USER");
        }
        return roles;
    }

    @Transactional
    public AuthResponseDTO signup(SignupRequestDTO request) {
        String profileUrl = null;
        MultipartFile profileImage = request.getProfileImage();

        if (profileImage != null && !profileImage.isEmpty()) {
            profileUrl = amazonS3Service.uploadFile(profileImage);
        }
        System.out.println("idToken : " + request.getIdToken());
        String email = fetchEmailFromIdToken(request.getIdToken());
        System.out.println(email);

        Member newMember = new Member();
        newMember.setUsername(request.getUsername());
        newMember.setEmail(email);
        newMember.setPassword(null);
        newMember.setOrganization(request.getOrganization());
        newMember.setMemberGrade(MemberGrade.DISABLE);
        newMember.setProfile(profileUrl);
        newMember.setCreatedAt(LocalDate.now());
        newMember.setDeleteStatus(DeleteStatus.ACTIVE);


        memberRepository.save(newMember);

        // 회원가입 후 JWT 발급
        String accessToken = jwtTokenProvider.createAccessToken(newMember.getEmail(), getRolesForMember(newMember));
        String refreshToken = jwtTokenProvider.createRefreshToken(newMember.getEmail());

        // 리프레시 토큰을 DB에 저장 (기존 토큰 덮어쓰기)
        LocalDateTime expirationTime = LocalDateTime.now().plusDays(15);
        refreshTokenRepository.save(new RefreshToken(newMember.getEmail(), refreshToken, expirationTime));

        return new AuthResponseDTO(true, accessToken, refreshToken, newMember, null);
    }

    // oauthId로 이메일 조회
    private String fetchEmailFromIdToken(String idToken) {
        System.out.println("idToken: " + idToken);

        // idToken을 검증하고 사용자 정보를 가져옴
        OAuth2UserInfoDTO userInfo = oAuth2TokenValidator.validateToken("google", idToken);

        if (userInfo == null || userInfo.getEmail() == null) {
            throw new IllegalArgumentException("OAuth2 ID Token으로 이메일 조회 실패");
        }

        return userInfo.getEmail(); // 이메일을 바로 반환
    }
    public String refreshAccessToken(String refreshToken) {
        // DB에서 리프레시 토큰 조회
        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("RefreshToken not found"));

        // 만료된 리프레시 토큰인지 확인
        if (storedToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(storedToken); // 만료된 토큰 삭제
            throw new IllegalArgumentException("RefreshToken이 만료되었습니다. 로그인을 다시 해주세요.");
        }

        // 유효한 경우 새로운 AccessToken 발급
        String email = jwtTokenProvider.parseClaims(refreshToken).getSubject();
        return jwtTokenProvider.createAccessToken(email, null); // 역할이 있다면 roles 추가
    }


}

