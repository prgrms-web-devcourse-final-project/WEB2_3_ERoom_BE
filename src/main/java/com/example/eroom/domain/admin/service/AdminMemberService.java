package com.example.eroom.domain.admin.service;


import com.example.eroom.domain.admin.repository.AdminMemberJPARepository;
import org.springframework.stereotype.Service;

@Service
public class AdminMemberService {
    private final AdminMemberJPARepository adminMemberJPARepository;

    public AdminMemberService(AdminMemberJPARepository adminMemberJPARepository) {
        this.adminMemberJPARepository = adminMemberJPARepository;
    }

    // 1. 전체 회원 조회
    /*
    public List<AdminMemberDTO> getMembers() {

    }
     */
}
