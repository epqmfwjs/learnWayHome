package com.learnway.study.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyProblemRepository extends JpaRepository<StudyProblem, Long> {
	
	StudyProblem findByStudyPostid(int postid);
	
	List<StudyProblem> findAllByStudyPostid(int postid);
	
	StudyProblem findTopByStudyPostidOrderByProblemidDesc(int postid);
}
