package com.learnway.study.dto;

import java.util.List;

import lombok.Data;

@Data
public class StudyTagDto {

	private Integer postid; 
	private List<String> tags;
//	private String tag;
	private List<String> tag;
}
