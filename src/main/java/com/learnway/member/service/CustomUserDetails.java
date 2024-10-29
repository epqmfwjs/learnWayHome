package com.learnway.member.service;

import com.learnway.member.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
// ★ 세션에 저장되는 사용자 정보라고 생각하면 됨
// CustomUserDetailsService 에서 반환된 UserDetails 인터페이스를 구현한 클래스
@RequiredArgsConstructor
@Getter
public class CustomUserDetails implements UserDetails {

    private final Member member;

    // ---------------------------- 커스텀 메서드 ----------------------------------------
    // 엔티티에서 세션에 저장 필요한 것들

    // 멤버 PK 반환 메서드
    public long getMemberId() {
        return member.getId();
    }
    // 사용자 이름 반환 메서드
    public String getName() {
        return member.getMemberName();
    }

    // 이미지
    public String getImage() {
        String memberImage = member.getMemberImage();
        System.out.println("Member Image URL: " + memberImage); // 이미지추적
        if (memberImage != null && !memberImage.isEmpty() && !memberImage.equals("/img/member/member-default.png")) {
            System.out.println("세션 이미지 저장 멤버아이디는 널이 아니였다!");
            return memberImage;
        } else {
            System.out.println("세션 이미지 저장 멤버아이디는 널이 였다!");
            return "/img/member/member-default.png"; // 기본 이미지 경로로 설정
        }
    }
    // --------------------------- 하기는 UserDetails 의 필수 구현 메서드 --------------------------------
    @Override
    // 사용자가 가진 권한을 컬렉션의 형태로 반환
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // MemberRole (ENUM) 값을 반환하여 name() 메서드를 이용해 문자열로 변환
        // 싱글톤 리스트로 감싸 반환
        return Collections.singletonList(new SimpleGrantedAuthority(member.getMemberRole().name()));
    }

    @Override // 유저 Password 반환
    public String getPassword() {
        return member.getMemberPw();
    }

    @Override // 유저 ID 반환
    public String getUsername() {
        return member.getMemberId();
    }

    @Override // 계정 만료 여부
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override // 계정 잠금 여부
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override // 자격 증명(비밀번호 등) 만료 여부
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override // 계정 활성화 여부
    public boolean isEnabled() {
        return true;
    }

}