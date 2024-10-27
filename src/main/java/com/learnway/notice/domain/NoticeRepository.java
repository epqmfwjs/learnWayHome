package com.learnway.notice.domain;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoticeRepository extends JpaRepository<Notice, Long>{
	Page<Notice> findAllByOrderByCreateDateDesc(Pageable pageable);
	Page<Notice> findByPriorityTrueOrderByCreateDateDesc(Pageable pageable);
	List<Notice> findAll();
	
	//상세페이지에서 이전글 다음글 찾기
	@Query(value = "SELECT * FROM notice "
			+ "WHERE notice_id = (SELECT prev_id FROM (SELECT notice_id, LAG(notice_id, 1, -1) OVER(ORDER BY notice_id) AS prev_id FROM notice) B "
			+ "WHERE notice_id = :id)", nativeQuery = true)
	Notice findPreNotice(@Param("id") Long id);
	
	@Query(value = "SELECT * FROM notice "
			+ "WHERE notice_id = (SELECT next_id FROM (SELECT notice_id, LEAD(notice_id, 1, -1) OVER(ORDER BY notice_id) AS next_id FROM notice) B "
			+ "WHERE notice_id = :id)", nativeQuery = true)
	Notice findNextNotice(@Param("id") Long id);
	
	Page<Notice> findByNoticeTitleContainingOrderByCreateDateDesc(Pageable pageable,String keyword);
	Page<Notice> findByCategoryContainingOrderByCreateDateDesc(String category, Pageable pageable);
	Page<Notice> findByNoticeTitleContainingAndCategoryContaining(String keyword, String category,
			Pageable pageable);
}
