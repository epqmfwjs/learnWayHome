package com.learnway.global.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// 기준 정보 리포지토리 인터페이스 - 학업 구분 : ex. 학교, 인강, 학원, 자습...
@Repository
public interface StudywayRepository extends JpaRepository<Studyway, String> {
    boolean existsByStudywayCode(String studywayCode);
}
