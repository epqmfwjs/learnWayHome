package com.learnway.notice.domain;

import java.time.LocalDateTime;
import java.util.Optional;

import com.learnway.member.domain.Member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notice {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)//자동증가
	private Long noticeId;
	
	@Column
	private String noticeTitle;
	
	@Column(columnDefinition = "TEXT")
	private String noticeContent;
	
	@Column
	private LocalDateTime createDate;
	
	@Column
	private String category;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", unique = false)
	private Member member; 
	
	@Transient
	private Notice preNotice;
	
	@Transient
	private Notice nextNotice;
	
	@Column(nullable=true)
	private String noticeImgUname;
	
	@Column(nullable=true)
	private String noticeImgPath;
	
	@Column(nullable=true)
	private boolean priority = false;
	
	
}
