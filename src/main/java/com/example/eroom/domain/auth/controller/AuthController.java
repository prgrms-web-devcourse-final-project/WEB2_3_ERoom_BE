package com.example.eroom.domain.auth.controller;

import com.amazonaws.services.s3.AmazonS3Client;
import com.example.eroom.domain.auth.dto.response.MemberResponseDTO;
import com.example.eroom.domain.auth.repository.AuthMemberRepository;
import com.example.eroom.domain.auth.security.CustomOAuth2Member;
import com.example.eroom.domain.auth.service.AmazonS3Service;
import com.example.eroom.domain.entity.DeleteStatus;
import com.example.eroom.domain.entity.Member;
import com.example.eroom.domain.entity.MemberGrade;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthMemberRepository memberRepository;
    private final HttpSession httpSession;
    private final AmazonS3Service amazonS3Service;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestParam String name,
                                    @RequestParam String organization,
                                    @RequestParam("profileImage") MultipartFile profileImage,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        String email = (String) httpSession.getAttribute("oauth_email");

        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        String profileUrl = amazonS3Service.uploadFile(profileImage);

        Member member = new Member();
        member.setEmail(email);
        member.setUsername(name);
        member.setProfile(profileUrl);
        member.setOrganization(organization);
        member.setMemberGrade(MemberGrade.DISABLE);
        member.setDeleteStatus(DeleteStatus.ACTIVE);
        memberRepository.save(member);

        CustomOAuth2Member customOAuth2Member = new CustomOAuth2Member(member, new HashMap<>());
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(customOAuth2Member, null, customOAuth2Member.getAuthorities());
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        HttpSessionSecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
        securityContextRepository.saveContext(securityContext, request, response);

        httpSession.setAttribute("member", member);

        return ResponseEntity.ok(Map.of(
                "message", "회원가입 성공",
                "email", member.getEmail(),
                "username", member.getUsername(),
                "profileUrl", member.getProfile()
        ));
    }
}
