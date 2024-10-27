package com.learnway.global.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// 기준 정보 리포지토리 인터페이스 - 과목 : ex.국어, 수학, 영어...
@Repository
public interface SubjectRepository extends JpaRepository<Subject, String> {
    boolean existsBySubjectCode(String subjectCode);
}
