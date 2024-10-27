package com.learnway.global.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamTRepository extends JpaRepository<ExamT, String> {
    boolean existsByExamCode(String examCode);
}
