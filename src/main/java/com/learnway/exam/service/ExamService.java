package com.learnway.exam.service;

import com.learnway.exam.domain.Exam;
import com.learnway.exam.domain.Score;
import com.learnway.exam.dto.ExamDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ExamService {

    public void writeExam(Exam exam);
    public Page<Exam> readExam(Long memId, int pageNo, int pageSize);
    public void updateExam(Exam exam);
    public void deleteExam(Long examId, Long memId);
    public Optional<Exam> findExamById(Long examId, Long memId);
    public ExamDetailDTO getExamDetail(Long examId, Long memId);
    public List<Exam> getExamsByExamType(Long memId, String examType);

    public Page<Exam> getExamsByExamType(Long memId, String examType, Pageable pageable);
    public Page<Exam> findScoreListByExamType(Long memId, String examType, Pageable pageable);
    public Page<Exam> findScoreList(Long memId, Pageable pageable);

    public Page<Score> getScoreListByExam(Long examId, Long memId, Pageable pageable);
    public Page<Score> getScoresByMemId(Long memId, Pageable pageable);
    public List<Score> getScoreListByMemId(Long memId);
    public Optional<Score> getScoreById(Long scoreId, Long memId);
    public Boolean writeScore(Score score, Long memId);
    public Boolean updateScore(Score score, Long memId);
    public void deleteScore(Long memId, Long scoreId);
    public List<Score> getScoreListBySubjectCode(Long memId, String subjectCode);
    public List<Score> getGrades(Long memId);
    public Page<Score> getScoreListByExamType(Long memId, String examType,Pageable pageable);
    public List<Integer> getAvgScores(Long memId);

    public List<Exam> findExamByExamTypeAndYear(Long memId, String examType, int year);
    public List<Exam> findExamByYear(Long memId, int year);
    public List<Exam> findAllExam(Long memId);

    public List<Exam> findExamByExamTypeAndDate(Long memId, String examType, Date startDate, Date endDate);
    public List<Exam> findExamByDate(Long memId, Date startDate, Date endDate);
}
