package com.example.comitserver.controller;

import com.example.comitserver.domain.Member;
import com.example.comitserver.dto.LoginDto;
import com.example.comitserver.dto.RegisterDto;
import com.example.comitserver.service.MemberServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

//@Controller
@Slf4j
public class MemberController {

    private MemberServiceImpl memberService;

    @GetMapping("/login")
    public String getLogin(HttpServletRequest request, Model model) {

        // 현재 페이지를 세션에 저장
        String referrer = request.getHeader("Referrer");
        request.getSession().setAttribute("prevPage", referrer);
        log.info("uri={}", referrer);
        model.addAttribute("login", new LoginDto());
        return "member/login";
    }

    @PostMapping
    public String postLogin(@ModelAttribute("login") LoginDto loginDto, HttpServletRequest request, HttpSession session, Model model) {
        boolean login = memberService.login(loginDto);

        if (login) {
            String username = loginDto.getUsername();
            Member member = memberService.findByUsername(username);
            session.setAttribute("loginMember", member);
            String prevPage = (String) session.getAttribute("prevPage"); // 저장된 페이지 주소 불러오기
            request.getSession().removeAttribute("prevPage"); // 세션에 저장한 페이지 삭제

            return "redirect:" + (prevPage != null ? prevPage : "/"); // 이전 페이지가 있으면 이전 페이지로
        }

        model.addAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
        return "member/login";
    }

    @GetMapping("/register")
    public String getRegister(Model model) {
        model.addAttribute("register", new RegisterDto());
        return "member/register";
    }

    @PostMapping("/register")
    public String postRegister(@ModelAttribute("register") RegisterDto registerDto, Model model) {
        try {
            memberService.register(registerDto);
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "member/register";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("loginMember");
        return "redirect:/";
    }
}
