package com.learnway.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    // 어드민 페이지로 이동
    @GetMapping("/main")
    public String adminMain() {
        return "admin/admin-main";
    }
}
