package com.example.eroom.domain.admin.service;

import com.example.eroom.domain.admin.dto.request.AdminUpdateMemberDTO;
import com.example.eroom.domain.admin.dto.response.AdminMemberDTO;
import com.example.eroom.domain.admin.repository.AdminMemberJPARepository;
import com.example.eroom.domain.chat.repository.MemberRepository;
import com.example.eroom.domain.entity.DeleteStatus;
import com.example.eroom.domain.entity.Member;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminMemberService {
    private final AdminMemberJPARepository adminMemberJPARepository;
    private final MemberRepository memberRepository;

    public AdminMemberService(AdminMemberJPARepository adminMemberJPARepository, MemberRepository memberRepository) {
        this.adminMemberJPARepository = adminMemberJPARepository;
        this.memberRepository = memberRepository;
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
    public AdminMemberDTO updateMember(Long memberId, AdminUpdateMemberDTO dto) {
        // 1. 회원 존재 여부 확인
        Member existingMember = adminMemberJPARepository.findById(memberId)
                .orElseThrow( () -> new EntityNotFoundException("해당 ID의 회원이 없습니다.: " + memberId));

        // 2. DTO로 받은 값을 기준으로 기존 엔티티 이름 변경
        existingMember.updateUserName(dto.getName());

        // 3. 수정된 엔티티 DB 저장
        Member updatedMember = memberRepository.save(existingMember);

        // 4. 엔티티 -> DTO로 변환
        return new AdminMemberDTO(updatedMember);
    }

    // [ 특정 회원 활성화]
    @Transactional
    public AdminMemberDTO activateMember(Long memberId) {
        // 1. 회원 존재 여부 확인
        Member member = adminMemberJPARepository.findById(memberId)
                .orElseThrow( () -> new EntityNotFoundException("해당 ID의 회원이 없습니다.: " + memberId));

        // 2. 비활성 -> 활성 전환
        member.activateUser();

        memberRepository.save(member);
        return new AdminMemberDTO(member);
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
