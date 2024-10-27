package com.learnway.study.service;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnway.member.domain.Member;
import com.learnway.member.domain.MemberRepository;
import com.learnway.study.domain.Study;
import com.learnway.study.domain.StudyProblem;
import com.learnway.study.domain.StudyProblemRepository;
import com.learnway.study.dto.StudyProblemDto;

@Service
public class StudyProblemService {

	@Autowired
	private StudyProblemRepository studyProblemRepository;
	@Autowired
	private MemberRepository memberRepository;
	
	//문제등록 및 해당문제id값 return
	public Integer problemAdd(StudyProblemDto dto,int postid,Principal principal) {
		
		//로그인한 값 가져와 수정예정
		Member member = memberRepository.findByMemberId(principal.getName())
	            .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + principal.getName()));
		
		StudyProblem studyProblem = StudyProblem.builder().subject(dto.getSubject())
									.level(dto.getLevel())
									.correct(dto.getCorrect()).study(Study.builder().postid(postid).build())
									.memid(member).build();
		studyProblemRepository.save(studyProblem);
		
		//해당문제 번호 구하기
		StudyProblem sp=studyProblemRepository.findByStudyPostid(postid);
		return sp.getProblemid(); //문제번호(문제이미지 테이블에서 사용)
		
	}
	
	//문제수정 메서드
	/*
	 * public Integer problemUpdate(StudyProblemDto dto,int postid,Principal principal) {
		
		//로그인한 값 가져와 수정예정
		Member member = memberRepository.findByMemberId(principal.getName())
				.orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + principal.getName()));
		StudyProblem problem = studyProblemRepository.findByStudyPostid(postid);
		
		StudyProblem studyProblem = StudyProblem.builder().subject(dto.getSubject())
				.problemid(problem.getProblemid())
				.level(dto.getLevel())
				.correct(dto.getCorrect()).study(Study.builder().postid(postid).build())
				.memid(member).build();
		studyProblemRepository.save(studyProblem);
		System.out.println(" -- 문제 수정 완료 --");
		//해당문제 번호 구하기
		StudyProblem sp=studyProblemRepository.findByStudyPostid(postid);
		System.out.println(" -- 문제 번호 조회 완료 --");
		return sp.getProblemid(); //문제번호(문제이미지 테이블에서 사용)
		
	} */
	public Integer problemUpdate(StudyProblemDto dto, int postid, Principal principal) {
	    // 로그인한 값 가져와 수정예정
	    Member member = memberRepository.findByMemberId(principal.getName())
	            .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + principal.getName()));
	    StudyProblem existingProblem = studyProblemRepository.findByStudyPostid(postid);

	    // Builder 패턴을 사용하여 객체 복제 및 수정
	    StudyProblem updatedProblem = existingProblem.toBuilder()
	            .subject(dto.getSubject())
	            .level(dto.getLevel())
	            .correct(dto.getCorrect())
	            .memid(member)
	            .build();

	    studyProblemRepository.save(updatedProblem);
	    System.out.println(" -- 문제 수정 완료 --");

	    // 해당문제 번호 구하기
	    StudyProblem sp = studyProblemRepository.findByStudyPostid(postid);
	    System.out.println(" -- 문제 번호 조회 완료 --");

	    return sp.getProblemid(); // 문제번호(문제이미지 테이블에서 사용)
	}
	
	public Integer problemId(int postId) {
		
		StudyProblem sp=studyProblemRepository.findByStudyPostid(postId);
		return sp.getProblemid();
	}
}
