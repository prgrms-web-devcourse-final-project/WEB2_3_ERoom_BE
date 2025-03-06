package com.example.eroom.domain.auth.controller;

import com.example.eroom.domain.auth.dto.request.MyPageUpdateRequestDTO;
import com.example.eroom.domain.auth.dto.response.MemberResponseDTO;
import com.example.eroom.domain.auth.repository.AuthMemberRepository;
import com.example.eroom.domain.auth.service.AmazonS3Service;
import com.example.eroom.domain.auth.service.MyPageService;
import com.example.eroom.domain.entity.Member;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final MyPageService myPageService;

    @GetMapping
    public ResponseEntity<MemberResponseDTO> getMyPage(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(new MemberResponseDTO(member));
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MemberResponseDTO> updateMyPage(
            @AuthenticationPrincipal Member member,
            @ModelAttribute MyPageUpdateRequestDTO request) {

        Member updatedMember = myPageService.updateMyPage(member, request, request.getProfileImage());
        return ResponseEntity.ok(new MemberResponseDTO(updatedMember));
    }

}



