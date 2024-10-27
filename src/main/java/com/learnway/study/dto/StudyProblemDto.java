package com.learnway.study.dto;

import lombok.Data;

@Data
public class StudyProblemDto {

	private Integer problemid;
	private String subject;
	private String level;
	private String content;
	private String correct;

}
