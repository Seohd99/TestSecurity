package com.example.testsecurity.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;
import java.util.Iterator;

@Controller
public class MainController {

    @GetMapping("/")
    public String mainP(Model model) {
        // 서비스 하나 만들기
        // 현재 로그인 아이디 불러오기
        String id = SecurityContextHolder.getContext().getAuthentication().getName();

        // 현재 로그인 롤 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        // 모델을 통하여 프론트로 넘기기
        model.addAttribute("id",id);
        model.addAttribute("role",role);
        return "main";
    }
}