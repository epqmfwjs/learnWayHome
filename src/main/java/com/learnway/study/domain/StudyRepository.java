package com.learnway.study.domain;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface StudyRepository extends JpaRepository<Study, Integer> {
	
	  @Query("SELECT s FROM Study s LEFT JOIN FETCH s.tags WHERE s.postid = :postid")
	    Optional<Study> findByIdWithTags(@Param("postid") Long postid);
	  
	  List<Study> findByTitleContaining(String title);
	  
	  List<Study> findByStartdateGreaterThanEqual(Date startDate);
	  
	  Page<Study> findByTitleContaining(String title, Pageable pageable);
	  
	  Page<Study> findByPostidIn(List<Integer> postIds, Pageable pageable);
	  
	  Page<Study> findByPostidIn(int[] detail, Pageable pageable);
	  
	  Study findByPostid(int postid);
	  
	  @Modifying
	  @Query("DELETE FROM Study s WHERE s.postid = :postid")
	  void deleteByPostid(@Param("postid") Integer postid);
}


