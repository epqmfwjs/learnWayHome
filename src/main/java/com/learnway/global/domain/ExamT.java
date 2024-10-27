package com.learnway.global.domain;

import jakarta.persistence.*;
import lombok.*;

// Main 기준 정보 엔티티 클래스 / 시험 종류
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamT {

    @Id
    @Column(length = 4)
    String examCode;       // 시험 종류 코드 (4자리, PK)

    @Column(nullable = false)
    String exam;           // 시험 종류명 (Not null)
    String examNote;         // 학습 종류 비고

}
