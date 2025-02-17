package com.example.eroom.domain.chat.controller;

import com.example.eroom.domain.chat.service.UserService;
import com.example.eroom.domain.entity.Member;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new Member());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute Member member) {
        userService.register(member);
        return "redirect:/user/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email, @RequestParam String password, HttpSession session) {
        Member member = userService.login(email, password);
        if (member != null) {
            session.setAttribute("user", member);
            return "redirect:/project/list";
        }
        return "redirect:/user/login?error";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user/login";
    }
}
