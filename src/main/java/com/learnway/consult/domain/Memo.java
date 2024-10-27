package com.learnway.consult.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Memo {
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	Long memoId;
	String memoTitle;
	String memoContents;
	
	@ManyToOne
    @JoinColumn(name = "consultant_id")
	Consultant consultant;
}
