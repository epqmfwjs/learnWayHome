package com.learnway.member.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // 로그인 방식 선택 - 일반 > 컨설트
    @GetMapping("/consult/loginChange/consult")
    public String loginChange_consult() {
        // 세션을 확인하여 로그인 되어 있는 상태일 경우 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            // 로그인 되어 있다면 권한 확인하여 권한에 따른 홈으로 리다이렉트 (로그인 시 로그인 창 접근 제한)
            return redirectBasedOnRole(authentication);
        }
        return "index-consult";
    }

    // 로그인 방식 선택 - 컨설트 > 일반
    @GetMapping("/loginChange/member")
    public String loginChange_member() {
        // 세션을 확인하여 로그인 되어 있는 상태일 경우 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            // 로그인 되어 있다면 권한 확인하여 권한에 따른 홈으로 리다이렉트 (로그인 시 로그인 창 접근 제한)
            return redirectBasedOnRole(authentication);
        }
        return "index";
    }

    // url ("/") 경로 설정
    @GetMapping("/")
    public String index() {
        // 현재 사용자의 인증 정보 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 사용자가 인증된 상태이고 비회원 유저가 아닌 경우
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication.getName().equals("anonymousUser")))  {
            return redirectBasedOnRole(authentication);
        }
        return "index";
    }

    // 권한에 따라 리다이렉트할 경로를 설정하는 메서드
    private String redirectBasedOnRole(Authentication authentication) {
        //ROLE 이 COUNSELOR 일 경우 consultant 로 보내기
        if (authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_COUNSELOR"))) {
            return "redirect:/consult/consultant";
        } else { // 아닐 경우 loginOk ( member, admin 권한 일 경우 )
            return "redirect:/loginOk";
        }
    }

    // 임시 테스트 로깅
    @GetMapping("/consult/home")
    public String consultHome() {
        return "consult-home";
    }
}
