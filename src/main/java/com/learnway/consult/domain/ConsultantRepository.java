package com.learnway.consult.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConsultantRepository extends JpaRepository<Consultant, Long> {
    Optional<Consultant> findByConsultantId(String consultantId);
}

