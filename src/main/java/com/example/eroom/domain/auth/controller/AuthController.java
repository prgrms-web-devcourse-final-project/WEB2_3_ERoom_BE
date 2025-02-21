package com.example.eroom.domain.auth.controller;

import com.example.eroom.domain.auth.repository.AuthMemberRepository;
import com.example.eroom.domain.auth.security.CustomOAuth2Member;
import com.example.eroom.domain.entity.DeleteStatus;
import com.example.eroom.domain.entity.Member;
import com.example.eroom.domain.entity.MemberGrade;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.ui.Model;

import java.util.HashMap;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthMemberRepository memberRepository;
    private final HttpSession httpSession;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("email", httpSession.getAttribute("oauth_email"));
        model.addAttribute("name", httpSession.getAttribute("oauth_name"));
        model.addAttribute("profile", httpSession.getAttribute("oauth_profile"));
        return "signup"; // signup.html 뷰 반환
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String organization, @RequestParam String profile) {
        String email = (String) httpSession.getAttribute("oauth_email");
        String name = (String) httpSession.getAttribute("oauth_name");

        // ✅ 회원 정보 저장
        Member member = new Member();
        member.setEmail(email);
        member.setUsername(name);
        member.setProfile(profile);
        member.setOrganization(organization);
        member.setMemberGrade(MemberGrade.DISABLE);  // 기본값 설정
        member.setDeleteStatus(DeleteStatus.ACTIVE);
        memberRepository.save(member);

        // ✅ 세션 초기화
        httpSession.removeAttribute("oauth_email");
        httpSession.removeAttribute("oauth_name");
        httpSession.removeAttribute("oauth_profile");

        return "redirect:/project/list";
    }
}
