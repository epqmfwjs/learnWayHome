package com.learnway.member.controller;

import com.learnway.member.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/email")
public class EmailRestController {

    private final EmailService emailService;

    // 이메일 인증 코드 발송
    @PostMapping("/send")
    @ResponseBody
    public ResponseEntity<String> sendVerificationEmail(@RequestParam("email") String email) {
        boolean isSent = emailService.sendVerificationEmail(email);
        if (isSent) {
            return ResponseEntity.ok("인증 코드가 이메일로 발송되었습니다.");
        } else {
            return ResponseEntity.status(500).body("이메일 발송에 실패했습니다.");
        }
    }

    // 이메일 인증 코드 확인
    @PostMapping("/verify")
    @ResponseBody
    public ResponseEntity<String> verifyEmail(@RequestParam("email") String email, @RequestParam("code") String code) {
        boolean isVerified = emailService.verifyEmail(email, code);
        if (isVerified) {
            return ResponseEntity.ok("이메일 인증이 완료되었습니다.");
        } else {
            return ResponseEntity.status(400).body("인증 코드가 올바르지 않습니다.");
        }
    }
}
