package com.learnway.study.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudyProblemImgRepository extends JpaRepository<StudyProblemImg, Long> {

	List<StudyProblemImg> findByStudyProblemProblemid(int problemId);
	
	@Query("SELECT spi FROM StudyProblemImg spi WHERE spi.studyProblem.problemid = :problemId")
    StudyProblemImg findOneByStudyProblemProblemid(@Param("problemId") int problemId);
	
}
