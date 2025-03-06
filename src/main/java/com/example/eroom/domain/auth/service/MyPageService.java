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

        if (requestDTO.getUsername() != null) {
            existingMember.setUsername(requestDTO.getUsername());
        }

        if (requestDTO.getOrganization() != null) {
            existingMember.setOrganization(requestDTO.getOrganization());
        }

        if (profileImage != null && !profileImage.isEmpty()) {
            if (existingMember.getProfile() != null) {
                amazonS3Service.deleteFile(existingMember.getProfile());
            }
            String newProfileUrl = amazonS3Service.uploadFile(profileImage);
            existingMember.setProfile(newProfileUrl);
        }

        return existingMember;
    }

}


