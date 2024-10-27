package com.learnway.study.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudyReplyResponseDto {

	private int postid;
	private String memberName;
	private String content;
	private LocalDateTime date;
}
