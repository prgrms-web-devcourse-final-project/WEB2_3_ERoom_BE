package com.example.eroom.domain.auth.thymeleaf;

import com.amazonaws.services.s3.AmazonS3Client;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthControllerEx {

    private HttpSessionSecurityContextRepository securityContextRepository;
    private final AuthMemberRepository memberRepository;
    private final HttpSession httpSession;
    private final AmazonS3Service amazonS3Service;

    @Autowired
    private AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("email", httpSession.getAttribute("oauth_email"));
        model.addAttribute("name", httpSession.getAttribute("oauth_name"));
        model.addAttribute("profile", httpSession.getAttribute("oauth_profile"));
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String name,
                         @RequestParam String organization,
                         @RequestParam("profileImage")MultipartFile profileImage,
                         HttpServletRequest request,
                         HttpServletResponse response) {
        String email = (String) httpSession.getAttribute("oauth_email");
        //String name = (String) httpSession.getAttribute("oauth_name");
        String profileUrl = amazonS3Service.uploadFile(profileImage);

        // 회원 정보 저장
        Member member = new Member();
        member.setEmail(email);
        member.setUsername(name);
        member.setProfile(profileUrl);
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
