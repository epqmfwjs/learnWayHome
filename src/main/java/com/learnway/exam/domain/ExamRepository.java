package com.learnway.exam.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    Page<Exam> findByMemIdOrderByExamDateDesc(Long memId, Pageable pageable);
    Page<Exam> findByMemIdAndExamType_ExamTypeNameOrderByExamDateDesc(Long memId, String examType, Pageable pageable);
    void deleteByMemIdAndExamId(Long memId, Long examId);
    Optional<Exam> findByMemIdAndExamId(Long memId, Long examId);
    List<Exam> findByMemIdAndExamType_ExamTypeName(Long memId, String examType);

    List<Exam> findAllByMemId(Long memId);
    @Query("SELECT e FROM Exam e WHERE YEAR(e.examDate) = :year AND e.memId = :memberId AND e.examType.examTypeName = :examTypeName")
    List<Exam> findExamsByYearMemberIdAndExamType(@Param("year") int year,
                                                  @Param("memberId") Long memberId,
                                                  @Param("examTypeName") String examTypeName);

    @Query("SELECT e FROM Exam e WHERE YEAR(e.examDate) = :year AND e.memId = :memberId")
    List<Exam> findExamsByYearMemberId(@Param("year") int year,
                                      @Param("memberId") Long memberId);

    @Query("SELECT e FROM Exam e WHERE e.examDate BETWEEN :startDate AND :endDate AND e.memId = :memberId AND e.examType.examTypeName = :examTypeName")
    List<Exam> findExamsByDateRangeMemberIdAndExamType(@Param("startDate") Date startDate,
                                                       @Param("endDate") Date endDate,
                                                       @Param("memberId") Long memberId,
                                                       @Param("examTypeName") String examTypeName);

    @Query("SELECT e FROM Exam e WHERE e.examDate BETWEEN :startDate AND :endDate AND e.memId = :memberId")
    List<Exam> findExamsByDateRangeMemberId(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("memberId") Long memberId);

}
