package com.example.eroom.domain.chat.thymeleaf.controller;

import com.example.eroom.domain.chat.thymeleaf.service.MemberService;
import com.example.eroom.entity.Member;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("member", new Member());
        return "register";
    }

    @PostMapping("/register")
    public String registerMember(@ModelAttribute Member member) {
        memberService.register(member);
        return "redirect:/member/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String loginMember(@RequestParam String email, @RequestParam String password, HttpSession session) {
        Member member = memberService.login(email, password);
        if (member != null) {
            session.setAttribute("member", member);
            return "redirect:/project/list";
        }
        return "redirect:/member/login?error";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/member/login";
    }
}
