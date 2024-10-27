package com.learnway.exam.dto;

import com.learnway.exam.domain.Score;
import com.learnway.global.domain.ExamType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamDetailDTO {
    Long examId;
    String examName;
    ExamType examType;
    String examRange;
    Date examDate;
    String examMemo;
    Page<Score> scoreList;
}
