package com.learnway.consult.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
public class Consultant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                    // PK
    private String consultantId;        // 컨설턴트 ID
    private String password;            // 비밀번호
    private String name;                // 컨설턴트 이름
    private String subject;             // 컨설턴트가 담당하는 과목
    @Column(length = 1000)
    private String description;         // 컨설턴트 설명
    private String imageUrl;            // 컨설턴트 프로필 이미지
    private String role;                // 컨설턴트 권한 : COUNSELOR 고정g


    public Consultant() {
    }

    public Consultant(Long id, String name, String subject, String description, String imageUrl) {
        this.id = id;
        this.name = name;
        this.subject = subject;
        this.description = description;
        this.imageUrl = imageUrl;
    }
}