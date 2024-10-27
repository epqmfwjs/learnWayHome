package com.learnway.member.service;

import com.learnway.member.domain.Member;
import com.learnway.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Spring Security 사용자 인증을 위한 사용자 세부 정보를 제공하는 커스텀 서비스
// 사용자 세부 정보를 로드하는 메서드가 정의되어 있는 UserDetailsService 인터페이스 구현
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    // 데이터베이스에서 사용자 정보를 조회하고, 이를 UserDetails 객체(인터페이스)로 변환하여 반환
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByMemberId(username)
                .orElseThrow(() -> new UsernameNotFoundException("유저가 없습니다"));
        return new CustomUserDetails(member); // 구현 클래스 : CustomUserDetails
    }
}
