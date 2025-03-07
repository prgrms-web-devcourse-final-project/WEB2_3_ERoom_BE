package com.example.eroom.domain.admin.service;

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
    public AdminMemberDTO updateMember(Long memberId, String name) {
        // 1. 회원 존재 여부 확인
        Member existingMember = adminMemberJPARepository.findById(memberId)
                .orElseThrow( () -> new EntityNotFoundException("해당 ID의 회원이 없습니다.: " + memberId));

        // 2. 값 변경 : 이름
        existingMember.updateUserName(name);

        // 3. 수정된 객체 DB 저장
        memberRepository.save(existingMember);

        // 3. 엔티티를 DTO로 변환
        AdminMemberDTO updatedMemberDTO = new AdminMemberDTO(
                existingMember.getId(),
                existingMember.getEmail(),
                existingMember.getUsername(),
                existingMember.getOrganization(),
                existingMember.getProfile(),
                existingMember.getCreatedAt()
        );

        // 4. 수정된 회원 DTO 반환
        return updatedMemberDTO;
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
