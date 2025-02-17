package com.example.eroom.domain.chat.service;

import com.example.eroom.domain.chat.repository.MemberRepository;
import com.example.eroom.domain.entity.Member;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void register(Member member) {
        memberRepository.save(member);
    }

    public Member login(String email, String password) {
        return memberRepository.findByEmailAndPassword(email, password);
    }

    // 현재 유저를 제외한 모든 유저 목록 가져오기
    public List<Member> getAllUsersExcept(Member currentMember) {
        return memberRepository.findAllByIdNot(currentMember.getId());
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }
}
