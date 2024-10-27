package com.learnway.consult.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {

	List<Memo> findByConsultantId(Long consultantId);

	List<Memo> findByMemoId(Long memoId);

	void deleteBymemoId(Long memoId);


}
