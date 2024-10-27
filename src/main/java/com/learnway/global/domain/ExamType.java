package com.learnway.global.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@Entity
@Table(name = "ExamType")
public class ExamType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exam_type_id", nullable = false)
    private Long examTypeId;

    @Column(name = "exma_type_name", nullable = false)
    private String examTypeName;

    @Builder
    public ExamType(Long examTypeId, String examTypeName) {
        this.examTypeId = examTypeId;
        this.examTypeName = examTypeName;
    }
}
