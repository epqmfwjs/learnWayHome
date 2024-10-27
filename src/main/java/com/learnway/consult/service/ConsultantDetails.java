package com.learnway.consult.service;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.learnway.consult.domain.Consultant;

import lombok.Getter;

// Counselor 멀티 로그인 테스트 파일
@Getter
public class ConsultantDetails implements UserDetails {

    private final Consultant consultant;

    // ---------------------------- 커스텀 메서드 ----------------------------------------
    // 멤버 PK 반환 메서드
    public long getId() {
        return consultant.getId();
    }

    public String getName(){ return consultant.getName(); }

    public ConsultantDetails(Consultant consultant) {
        this.consultant = consultant;
    }

    public String getImage() {

        String consultImage = consultant.getImageUrl();
        if (consultImage != null && !consultImage.isEmpty() && !consultImage.equals("/img/member/member-default.png")) {
            return /*"/api/member/uploads/" +*/ consultImage;
        } else {
            return "/img/member/member-default.png"; // 기본 이미지 경로로 설정
        }
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_COUNSELOR"));
    }

    @Override
    public String getPassword() {
        return consultant.getPassword();
    }

    @Override
    public String getUsername() {
        return consultant.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
    
}