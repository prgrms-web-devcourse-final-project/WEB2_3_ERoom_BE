package com.example.eroom.domain.auth.controller;

import com.example.eroom.domain.auth.repository.AuthMemberRepository;
import com.example.eroom.domain.auth.security.CustomOAuth2Member;
import com.example.eroom.domain.entity.DeleteStatus;
import com.example.eroom.domain.entity.Member;
import com.example.eroom.domain.entity.MemberGrade;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    private HttpSessionSecurityContextRepository securityContextRepository;
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
    public String signup(@RequestParam String organization,
                         @RequestParam String profile,
                         HttpServletRequest request,
                         HttpServletResponse response) {
        String email = (String) httpSession.getAttribute("oauth_email");
        String name = (String) httpSession.getAttribute("oauth_name");

        // 회원 정보 저장
        Member member = new Member();
        member.setEmail(email);
        member.setUsername(name);
        member.setProfile(profile);
        member.setOrganization(organization);
        member.setMemberGrade(MemberGrade.DISABLE);
        member.setDeleteStatus(DeleteStatus.ACTIVE);
        memberRepository.save(member);

        // Spring Security 인증 설정 (자동 로그인)
        CustomOAuth2Member customOAuth2Member = new CustomOAuth2Member(member, new HashMap<>());
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(customOAuth2Member, null, customOAuth2Member.getAuthorities());
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        HttpSessionSecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
        securityContextRepository.saveContext(securityContext, request, response);

        // 세션에 member 저장
        httpSession.setAttribute("member", member);

        return "redirect:/project/list";
    }
}
