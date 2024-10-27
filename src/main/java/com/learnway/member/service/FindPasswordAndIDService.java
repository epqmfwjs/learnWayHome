package com.learnway.member.service;

import com.learnway.member.domain.Member;
import com.learnway.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FindPasswordAndIDService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 멤버 ID, Email 조회
    public boolean validateUsernameAndEmail(String username, String email) {
        return memberRepository.findByMemberIdAndMemberEmail(username, email).isPresent();
    }

    // 인증 후 패스워드 리셋
    public void updatePassword(String username, String newPassword) {
        Member member = memberRepository.findByMemberId(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        String encodedPassword = bCryptPasswordEncoder.encode(newPassword);
        Member updatedMember = member.toBuilder()
                .memberPw(encodedPassword)
                .build();
        memberRepository.save(updatedMember);
    }

    // 멤버 이름과 Email 조회 -> ID 찾기
    public List<String> findUserIdsByNameAndEmail(String name, String email) {
        List<Member> members = memberRepository.findAllByMemberNameAndMemberEmail(name, email);
        if (members.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }
        return members.stream()
                .map(Member::getMemberId)
                .collect(Collectors.toList());
    }
}
