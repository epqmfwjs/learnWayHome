package com.learnway.global.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// 기준 정보 리포지토리 인터페이스 - 학습 종류 : ex. 교과서, 교재, 프린트물...
@Repository
public interface MaterialRepository extends JpaRepository<Material, String> {
    boolean existsByMaterialCode(String materialCode);
}
