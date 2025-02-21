package com.example.eroom.domain.admin.service;


import com.example.eroom.domain.admin.dto.request.AdminUpdateMemberDTO;
import com.example.eroom.domain.admin.dto.response.AdminMemberDTO;
import com.example.eroom.domain.admin.repository.AdminMemberJPARepository;
import com.example.eroom.domain.entity.DeleteStatus;
import com.example.eroom.domain.entity.Member;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminMemberService {
    private final AdminMemberJPARepository adminMemberJPARepository;

    public AdminMemberService(AdminMemberJPARepository adminMemberJPARepository) {
        this.adminMemberJPARepository = adminMemberJPARepository;
    }

    // [ 전체 활성화 회원 목록 ]
    public List<AdminMemberDTO> getActiveMembers() {
        return adminMemberJPARepository.findByDeleteStatus(DeleteStatus.ACTIVE)
                .stream()
                .map(AdminMemberDTO::new)
                .collect(Collectors.toList());
    }

    // [ 전체 비활성화 회원 목록 ]
    public List<AdminMemberDTO> getInActiveMembers() {
        return adminMemberJPARepository.findByDeleteStatus(DeleteStatus.DELETED)
                .stream()
                .map(AdminMemberDTO::new)
                .collect(Collectors.toList());
    }

    // [ 특정 회원 수정 ]
    public AdminMemberDTO updateMember(Long memberId, AdminUpdateMemberDTO updatedMemberDTO) {
        // 1. 회원 존재 여부 확인
        Member existingMember = adminMemberJPARepository.findById(memberId)
                .orElseThrow( () -> new EntityNotFoundException("해당 ID의 회원이 없습니다.: " + memberId));

        // 2. 값 업데이트 : 이름, 등록일, 구독 여부, 소속, 프로필 이미지
        // 필드 값이 Null이 아닐 때만 업데이트 실행
        if (updatedMemberDTO.getUsername() != null) {
            existingMember.setUsername(updatedMemberDTO.getUsername());
        }
        if (updatedMemberDTO.getCreatedAt() != null) {
            existingMember.setCreatedAt(updatedMemberDTO.getCreatedAt());
        }
        if (updatedMemberDTO.getMemberGrade() != null) {
            existingMember.setMemberGrade(updatedMemberDTO.getMemberGrade());
        }
        if (updatedMemberDTO.getOrganization() != null) {
            existingMember.setOrganization(updatedMemberDTO.getOrganization());
        }
        if (updatedMemberDTO.getProfile() != null) {
            existingMember.setProfile(updatedMemberDTO.getProfile());
        }

        // 3. 변경된 데이터 저장
        Member savedMember = adminMemberJPARepository.save(existingMember);

        // 4. DTO로 변환하여 저장
        return new AdminMemberDTO(savedMember);
    }

    // [ 특정 회원 삭제 ]
    public void deleteMember(Long memberId) {
        // 1. 회원 존재 여부 확인
        Member existingMember = adminMemberJPARepository.findById(memberId)
                .orElseThrow( () -> new EntityNotFoundException("해당 ID의 회원이 없습니다.: " + memberId));

        // 2. 회원 삭제
        adminMemberJPARepository.delete(existingMember);
    }
}
