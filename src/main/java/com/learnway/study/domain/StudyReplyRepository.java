package com.learnway.study.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudyReplyRepository extends JpaRepository<StudyReply, Integer> {

	@Query("SELECT sr FROM StudyReply sr WHERE sr.study.postid = :postId ORDER BY sr.date DESC")
	List<StudyReply> findByStudy_PostidOrderByDateDesc(@Param("postId") int postId);
}
