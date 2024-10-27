package com.learnway.member.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import com.learnway.notice.domain.Notice;
import com.learnway.schedule.domain.Schedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Member 관련 엔티티 클래스
@Getter
@Builder(toBuilder = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB가 기본 키 값을 자동으로 관리 (MySQL -> AUTO INCREMENT)
    private Long id;                    // PK

    @Column(unique = true, nullable = false)
    private String memberId;            // ID (Not Null, Unique)

    @Column(nullable = false)
    private String memberPw;            // Password (Not Null)

    @Column(nullable = false)
    private String memberName;          // 성함 (Not Null)

    @Column(nullable = false)
    private LocalDate memberBirth;      // 생년월일 (Not Null)

    @Column(nullable = false)
    private String memberPhone;         // 연락처 (Not Null)

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberTelecom memberTelecom;// 통신사 (KT, SKT, LG, 동 알뜰폰)

    @Column(nullable = false)
    private String memberEmail;         // E-mail

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberGender memberGender;  // 성별 (Male / Female)

    @CreationTimestamp
    private LocalDate memberCreate = LocalDate.now(); // 가입일 (가입 당시 시간 자동 입력)

    private String memberSchool;        // 학교

    @Column(nullable = true)
    private int memberGrade;            // 학년 (1~6 숫자)
    private String memberAddress;       // 주소
    private String memberDetailadd;     // 상세 주소 (주소 외 나머지 주소)
    private String memberImage;         // 프로필 이미지 / 기본 이미지 경로 추가 설정 필요

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;      // 회원 관리자 구분 (Enum : ROLE_ADMIN, ROLE_USER)

    private String memberNote;          // 회원 비고 (관리자(선생님)이 학생에게 메모해둘 사항 (학생은 조회 X)

    // JOIN : 목표 대학
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default // 빌드 시 해당 필드 초기화
    private List<TargetUni> targetUnis = new ArrayList<>();

    public void addTargetUni(TargetUni targetUni) {
        targetUnis.add(targetUni);
    }

}