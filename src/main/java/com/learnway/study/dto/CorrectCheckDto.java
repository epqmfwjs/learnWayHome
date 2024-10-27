package com.learnway.study.dto;

import com.learnway.member.domain.Member;
import com.learnway.study.domain.Study;

import lombok.Data;

@Data
public class CorrectCheckDto {

	
	private int correctId;
	private String roomId;
	private int postId;
	
//	private Member member;
//	private Study study;
	private int status;
}
