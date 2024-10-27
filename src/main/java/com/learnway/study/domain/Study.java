package com.learnway.study.domain;

import java.sql.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name="study")
public class Study {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="study_postid" , nullable = false)
	private Integer postid;
	
	
	@Column(name="study_title")
	private String title;
	
	@Column(name="study_content",length = 100000)
	private String content;
	
	@Column(name="study_viewcount")
	private String viewcount;
	
	@Column(name="study_createdate")
	@CreationTimestamp
	private Date createdate;
	
	@Column(name="study_startdate",nullable = true)
	private Date startdate;
	
	@Column(name="study_enddate",nullable = true)
	private Date enddate;
	
	@Column(name="study_isjoin",nullable = false)
	private byte isjoin;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id", nullable = false)
	private Member member;
	
	@OneToMany(mappedBy = "study", cascade = CascadeType.REMOVE)
	@JsonManagedReference
	private List<StudyReply> replies;
	
	@OneToMany(mappedBy = "study", cascade = CascadeType.REMOVE)
	@JsonManagedReference
	private List<ChatRoom> chatroom;

	@OneToMany(mappedBy = "study", cascade = CascadeType.REMOVE)
	@JsonManagedReference
	private List<StudyTag> tags;
	
	@OneToMany(mappedBy = "study", cascade = CascadeType.REMOVE)
	@JsonManagedReference
	private List<CorrectCheck> correctCheck;
	
	

	
	 
	 
	
	 
	@Transient
	private int repliesCount;
	
	@Transient
	private int chatroomCount;
	
	@PostLoad
	private void calculateRepliesCount() {
	this.repliesCount = this.replies != null ? this.replies.size() : 0;
	this.chatroomCount = this.chatroom != null ? this.chatroom.size() : 0;
	    }
	
}
