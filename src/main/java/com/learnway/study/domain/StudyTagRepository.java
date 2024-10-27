package com.learnway.study.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudyTagRepository extends JpaRepository<StudyTag, Integer> {
    
    List<StudyTag> findByStudyPostid(int postId);
    
    @Query("SELECT st.study.postid FROM StudyTag st WHERE st.tag IN :tags GROUP BY st.study.postid HAVING COUNT(DISTINCT st.tag) = :tagCount")
    List<Integer> findPostIdsByTags(@Param("tags") List<String> tags, @Param("tagCount") long tagCount);
}
