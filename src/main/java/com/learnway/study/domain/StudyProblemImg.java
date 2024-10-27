package com.learnway.study.domain;

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

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Getter
@Table(name="Problems_img")
public class StudyProblemImg {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="study_pbimgid" , nullable = false)
	private Integer pbimgid;
	
	@Column(name="study_correct")
	private String correct;
	
	@Column(name="study_imgdir")
	private String imgdir;
	
	@Column(name="study_imgpath")
	private String imgpath;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "study_problemid", nullable = false)
	private StudyProblem studyProblem;
}
