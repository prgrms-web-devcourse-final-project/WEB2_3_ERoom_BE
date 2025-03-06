package com.example.eroom.domain.auth.service;

import com.example.eroom.domain.auth.dto.request.MyPageUpdateRequestDTO;
import com.example.eroom.domain.auth.repository.AuthMemberRepository;
import com.example.eroom.domain.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyPageService {

    private final AuthMemberRepository memberRepository;
    private final AmazonS3Service amazonS3Service;
    private final EntityManager entityManager;

    @Transactional
    public Member updateMyPage(Member member, MyPageUpdateRequestDTO requestDTO, MultipartFile profileImage) {
        Member existingMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        Member updatedMember = existingMember.toBuilder()
                .username(requestDTO.getUsername() != null ? requestDTO.getUsername() : existingMember.getUsername())
                .organization(requestDTO.getOrganization() != null ? requestDTO.getOrganization() : existingMember.getOrganization())
                .build();

        if (profileImage != null && !profileImage.isEmpty()) {
            // 기존 프로필 이미지 삭제
            if (existingMember.getProfile() != null) {
                amazonS3Service.deleteFile(existingMember.getProfile());
            }
            // 새 이미지 업로드
            String newProfileUrl = amazonS3Service.uploadFile(profileImage);

            updatedMember = updatedMember.toBuilder()
                    .profile(newProfileUrl)
                    .build();
        }

        memberRepository.save(updatedMember);

        return updatedMember;
    }

}


