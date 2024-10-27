package com.learnway.global.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

// Main 기준 정보 엔티티 클래스 / 학업 구분 : ex. 학교, 인강, 학원, 자습 리포지토리 인터페이스
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Studyway {

    @Id
    @Column(length = 4)
    String studywayCode;       // 학업 구분 코드 ( 4자리, PK )

    @Column(nullable = false)
    String studyway;           // 학업 구분명 (Not null)
    String studywayNote;       // 과목 비고
    
    // getName() 메서드 추가
    public String getName() {
        return this.studyway;
    }
}
