package com.learnway.study.domain;


import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;


public class StudyTagRepositoryImpl implements StudyTagRepositoryCustom {

    @PersistenceContext
    private  EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<StudyTag> findByTag(List<String> tags) {
        // JPQL 쿼리 작성
        String jpql = "SELECT s FROM StudyTag s WHERE 1=1";

        // 각 태그를 모두 포함하는 엔티티를 찾기 위해 LIKE 구문 사용
        for (String tag : tags) {
            jpql += " AND s.tag LIKE CONCAT('%', :tag, '%')";
        }

        // 쿼리 실행
        TypedQuery<StudyTag> query = entityManager.createQuery(jpql, StudyTag.class);

        // 파라미터 설정
        for (String tag : tags) {
            query.setParameter("tag", tag);
        }

        // 결과 반환
        return query.getResultList();
    }



}
