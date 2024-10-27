package com.learnway.member.controller;

import com.learnway.member.dto.PasswordResetDTO;
import com.learnway.member.service.EmailService;
import com.learnway.member.service.FindPasswordAndIDService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/password")
@RequiredArgsConstructor
public class FindPasswordAndIDController {

    private final FindPasswordAndIDService findPasswordAndIDService;
    private final EmailService emailService;

    // GET : 비밀번호 찾기 폼.
    @GetMapping("/find")
    public String showPasswordFindForm() {
        return "member/password-find";
    }

    // POST : ID,Email 일치 여부 확인 후 이메일 발송 및 에러 모달
    @PostMapping("/find")
    @ResponseBody
    public ResponseEntity<String> findPassword(@RequestParam("username") String username, @RequestParam("email") String email, HttpSession session) {
        boolean isValidUser = findPasswordAndIDService.validateUsernameAndEmail(username, email);
        if (isValidUser) {
            boolean isEmailSent = emailService.sendVerificationEmail(email);
            if (isEmailSent) {
                session.setAttribute("authenticatedUsername", username); // 비밀번호 재설정 접근 제한을 위해 세션에 인증된 사용자 이름 저장
                return ResponseEntity.ok("OK");
            } else {
                return ResponseEntity.status(500).body("이메일 발송에 실패했습니다.");
            }
        } else { // 불일치 시 오류 모달
            return ResponseEntity.badRequest().body("ID 혹은 이메일을 확인해 주세요.");
        }
    }

    // 이메일과 인증 코드를 확인하여 세션에 인증 정보(ID)를 저장
    @PostMapping("/verify")
    @ResponseBody
    public ResponseEntity<String> verifyEmail(@RequestParam("email") String email, @RequestParam("code") String code, HttpSession session) {
        boolean isVerified = emailService.verifyEmail(email, code);
        if (isVerified) {
            session.setAttribute("authenticatedUsername", (String) session.getAttribute("authenticatedUsername")); // 비밀번호 재설정 접근 제한을 위해 세션에 인증된 이메일 저장
            return ResponseEntity.ok("OK");
        } else { // 불일치 시 에러 메세지 전달
            return ResponseEntity.badRequest().body("인증 코드가 올바르지 않습니다.");
        }
    }

    // 비밀번호 재설정 폼
    @GetMapping("/reset")
    public String showPasswordResetForm(HttpSession session, Model model) {
        // password reset 페이지로 이동하기 위해 세션에 저장된 ID(username)를 체크
        String authenticatedUsername = (String) session.getAttribute("authenticatedUsername");
        if (authenticatedUsername == null) {
            return "redirect:/password/find"; // 인증되지 않은 경우 비밀번호 찾기 페이지로 리다이렉트
        }
        model.addAttribute("passwordResetDTO", new PasswordResetDTO());
        return "member/password-reset";
    }

    // 비밀번호 재설정
    @PostMapping("/reset")
    public String resetPassword(@Valid @ModelAttribute("passwordResetDTO") PasswordResetDTO passwordResetDTO, BindingResult bindingResult, HttpSession session, RedirectAttributes redirectAttributes) {
        String authenticatedUsername = (String) session.getAttribute("authenticatedUsername");
        if (authenticatedUsername == null) {
            return "redirect:/password/find"; // 인증되지 않은 경우 비밀번호 찾기 페이지로 리다이렉트
        }
        // 컨펌 비밀번호, 비밀번호 일치 여부 확인 후 불일치 시 에러메세지 전달
        if (bindingResult.hasErrors() || !passwordResetDTO.getPassword().equals(passwordResetDTO.getConfirmPassword())) {
            if (!passwordResetDTO.getPassword().equals(passwordResetDTO.getConfirmPassword())) {
                bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "비밀번호가 일치하지 않습니다.");
            }
            return "member/password-reset";
        }
        // 비밀번호 업데이트
        findPasswordAndIDService.updatePassword(authenticatedUsername, passwordResetDTO.getPassword());
        session.removeAttribute("authenticatedUsername"); // 세션에서 인증 정보 제거
        // 하기 메세지 인덱스에서 출력
        redirectAttributes.addFlashAttribute("message", "비밀번호 변경이 완료되었습니다. 변경된 비밀번호로 로그인 해주세요.");
        return "redirect:/?passwordResetSuccess=true";
    }

    // GET : ID 찾기 폼
    @GetMapping("/id/find")
    public String showIdFindForm() {
        return "member/id-find";
    }

    // POST : 이름, 이메일 일치 여부 확인 후 아이디 반환
    @PostMapping("/id/find")
    @ResponseBody
    public ResponseEntity<?> findId(@RequestParam("name") String name, @RequestParam("email") String email) {
        List<String> userIds = findPasswordAndIDService.findUserIdsByNameAndEmail(name, email);
        if (!userIds.isEmpty()) {
            // 중복된 결과일 경우 모두 반환
            List<String> maskedUserIds = userIds.stream()
                    .map(this::hideId) // 중간 글자 마스킹 처리
                    .collect(Collectors.toList());
            return ResponseEntity.ok(maskedUserIds);
        } else {
            return ResponseEntity.badRequest().body("이름과 이메일을 확인해 주세요.");
        }
    }

    // 아이디 조회 시 3,4번째 글자 * 표시
    private String hideId(String userId) {
        if (userId.length() < 4) {
            return userId;
        }
        StringBuilder masked = new StringBuilder(userId);
        masked.setCharAt(2, '*');
        masked.setCharAt(3, '*');
        return masked.toString();
    }
}
