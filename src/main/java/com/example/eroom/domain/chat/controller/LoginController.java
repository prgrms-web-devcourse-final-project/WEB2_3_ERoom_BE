package com.example.eroom.domain.chat.controller;

import com.example.eroom.domain.chat.service.MemberService;
import com.example.eroom.domain.entity.Member;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LoginController {

    private final MemberService memberService;

//    {
//        "email" : "qwerty1@gmail.com",
//            "password" : 1234
//    }

    @Getter
    @Setter
    public static class LoginRequest {
        private String email;
        private String password;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request, HttpSession session) {

        System.out.println("hello login");

        Member member = memberService.login(request.getEmail(), request.getPassword());

        System.out.println("currentMember : " + member);

        if (member != null) {
            session.setAttribute("member", member);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login/querystring")
    public ResponseEntity<Void> loginQueryString(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session) {

        System.out.println("hello login");

        Member member = memberService.login(email, password);

        System.out.println("currentMember : " + member);

        if (member != null) {
            session.setAttribute("member", member);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/session-check")
    public ResponseEntity<Member> checkSession(HttpSession session) {
        Member member = (Member) session.getAttribute("member");

        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(member);
    }
}
