package com.learnway.study.dto;

import java.sql.Date;
import java.util.List;

import lombok.Data;

@Data
public class StudyDto {
	
	
	private Integer postid;
	
	private Integer id;
	
	private String title;
	
	private String content;
	
	private String viewcount;
	
	private Date createdate;
	
	private Date startdate;
	private Date enddate;
	
	
	private String startdatetest;
	private String enddatetest;
	
	private byte isjoin;
	
	private String search;
	private String detailSearch;
	private int[] detailSearchArray;
	
	
	
}
