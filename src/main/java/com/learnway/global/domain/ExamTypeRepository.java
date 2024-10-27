package com.learnway.global.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamTypeRepository extends JpaRepository<ExamType, Long> {
    Optional<ExamType> findByExamTypeName(String name);
}
