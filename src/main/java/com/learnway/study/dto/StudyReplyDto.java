package com.learnway.study.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudyReplyDto {

	
	public StudyReplyDto(Integer postid, String content, String memberName, LocalDateTime date) {
		this.postId = postid;
		this.content = content;
		this.memberName = memberName;
		this.date = date;
	}
	private Integer commentId;
	private String content;
	private LocalDateTime date;
	private LocalDateTime datetime;
	
	private String likes;
	private String memberId;
	private String memberName;
	private Integer postId;
	
}
