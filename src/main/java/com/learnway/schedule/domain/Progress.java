package com.learnway.schedule.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.learnway.global.domain.Material;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Progress {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)//자동증가
	private Long progressId;
	
	@Column(nullable=true)
	private double achieveRate;
	
	@Column
	private String progress;
	
	@Column
	private String memberId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "scheduleId")
	private Schedule scheduleId;

	@ManyToOne
	@JoinColumn(name = "materialId",unique = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Material materialId;
}
