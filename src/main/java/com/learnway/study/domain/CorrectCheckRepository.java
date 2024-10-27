package com.learnway.study.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learnway.member.domain.Member;

public interface CorrectCheckRepository extends JpaRepository<CorrectCheck, Long> {
	 List<CorrectCheck> findByMember(Member member);
	 List<CorrectCheck> findByStudy_Postid(Integer postid);
}
