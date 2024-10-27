package com.learnway.study.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.learnway.member.domain.Member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="studyReply")
public class StudyReply {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="comment_id" , nullable = false)
	private Integer commentId;
	
	@Column(name="content")
	private String content;
	
	@Column(name="content_date")
	private LocalDateTime date;
	
	@Column(name="likes")
	private String likes;
	
	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "id", nullable = false)
    private Member member;
	
	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "study_postid", nullable = false)
	@JsonBackReference
    private Study study;
}
