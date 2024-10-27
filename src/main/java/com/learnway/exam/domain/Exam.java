package com.learnway.exam.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.learnway.global.domain.ExamType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Exam")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exam_id", nullable = false)
    private Long examId;

    @Column(name = "member_id", nullable = false)
    private Long memId;

    @Column(name = "exam_name", nullable = false)
    private String examName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exam_type_name")
    private ExamType examType;

    @Column(name = "exam_date", nullable = false)
    private Date examDate;

    @Column(name = "exam_end_date", nullable = true)
    private Date examEndDate;


    @Column(name = "exam_memo", nullable = true)
    private String examMemo;

    @Transient
    @JsonManagedReference
    private List<Score> scoreList;

    @Builder
    public Exam(Long examId, Long memId, String examName, ExamType examType, Date examDate, Date examEndDate, String examMemo, List<Score> scoreList) {
        this.examId = examId;
        this.memId = memId;
        this.examName = examName;
        this.examType = examType;
        this.examDate = examDate;
        this.examEndDate = examEndDate;
        this.examMemo = examMemo;
        this.scoreList = scoreList;
    }
}
