package com.example.eroom.domain.auth.controller;

import com.example.eroom.domain.auth.dto.response.MemberResponseDTO;
import com.example.eroom.domain.auth.repository.AuthMemberRepository;
import com.example.eroom.domain.auth.service.AmazonS3Service;
import com.example.eroom.domain.entity.Member;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageController {

    private final AuthMemberRepository memberRepository;
    private final AmazonS3Service amazonS3Service;
    private final HttpSession httpSession;

    @GetMapping
    public ResponseEntity<MemberResponseDTO> getMyProfile() {
        Member member = getLoggedInMember();

        MemberResponseDTO responseDTO = new MemberResponseDTO(
                member.getId(),
                member.getEmail(),
                member.getUsername(),
                member.getOrganization(),
                member.getProfile()
        );

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MemberResponseDTO> updateMyProfile(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String organization,
            @RequestParam(required = false) MultipartFile profileImage) {

        Member member = getLoggedInMember();

        if (username != null && !username.isEmpty()) {
            member.setUsername(username);
        }

        if (organization != null && !organization.isEmpty()) {
            member.setOrganization(organization);
        }

        if (profileImage != null && !profileImage.isEmpty()) {
            if (member.getProfile() != null && !member.getProfile().isEmpty()) {
                amazonS3Service.deleteFile(member.getProfile());
            }

            String newImageUrl = amazonS3Service.uploadFile(profileImage);
            member.setProfile(newImageUrl);
        }

        memberRepository.save(member);

        MemberResponseDTO responseDTO = new MemberResponseDTO(
                member.getId(),
                member.getEmail(),
                member.getUsername(),
                member.getOrganization(),
                member.getProfile()
        );

        return ResponseEntity.ok(responseDTO);
    }

    private Member getLoggedInMember() {
        Member member = (Member) httpSession.getAttribute("member");
        if (member == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        return member;
    }
}



