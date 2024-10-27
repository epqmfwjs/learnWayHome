package com.learnway.member.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TargetUni {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uniId; // 목표 대학 식별키

    @Column(nullable = false)
    private String uniName; // 목표 대학(과) 이름

    private String uniRank; // 지망 순위 : 1순위 / 2순위 / 3순위

    @ManyToOne // 다 대 1 / FK
    @JoinColumn(name = "member_id", nullable = false)
    @JsonIgnore // 순환 참조 방지 : 양방향으로 매핑이 있을 경우 순환 참조 문제를 방지를 위해 사용
    //자바 <-> json 변환(직렬,역직렬) 시 해당 에너테이션을 사용한 필드를 무시함
    private Member member;
}