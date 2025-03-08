package com.example.eroom.domain.auth.service;

import com.example.eroom.domain.auth.dto.request.MyPageUpdateRequestDTO;
import com.example.eroom.domain.auth.repository.AuthMemberRepository;
import com.example.eroom.domain.entity.DeleteStatus;
import com.example.eroom.domain.entity.Member;
import com.example.eroom.domain.entity.MemberGrade;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class MyPageService {

    private final AuthMemberRepository memberRepository;
    private final AmazonS3Service amazonS3Service;
    private final EntityManager entityManager;

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
        } else { // 이미지가 null이라면
            if (existingMember.getProfile() != null) {
                amazonS3Service.deleteFile(existingMember.getProfile());
            }
            updatedMember = updatedMember.toBuilder()
                    .profile(null) // 프로필을 null로 설정
                    .build();
        }

        memberRepository.save(updatedMember);

        return updatedMember;
    }

    // 회원 탈퇴
    public void deleteMember(Member member) {
        Member updatedMember = member.toBuilder()
                .deleteStatus(DeleteStatus.DELETED)
                .build();
        memberRepository.save(updatedMember); // 변경된 상태로 저장
    }

}


