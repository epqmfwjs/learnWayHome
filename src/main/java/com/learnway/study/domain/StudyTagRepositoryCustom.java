package com.learnway.study.domain;

import java.util.List;

public interface StudyTagRepositoryCustom {
	
	List<StudyTag> findByTag(List<String> tags);
	
}
