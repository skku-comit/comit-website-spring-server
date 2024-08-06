package com.example.comitserver.controller;

import com.example.comitserver.domain.Member;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello Spring!";
    }

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");

        model.addAttribute("loginMember", loginMember);
        return "home";
    }
}
