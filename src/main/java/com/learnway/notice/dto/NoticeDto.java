package com.learnway.notice.dto;

import java.time.LocalDateTime;

import com.learnway.member.domain.Member;
import com.learnway.notice.domain.Notice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDto {
	
	private Long noticeId;
	private String noticeTitle;
	private String noticeContent;
	private String noticeImgUname;
	private String noticeImgPath;
	private Notice preNotice;
	private Notice nextNotice;
	private LocalDateTime createDate;
	private boolean priority;
	private Member memberId; 
	private String category;
}
