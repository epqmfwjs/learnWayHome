package com.learnway.study.service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnway.member.domain.Member;
import com.learnway.member.domain.MemberRepository;
import com.learnway.study.domain.StudyReply;
import com.learnway.study.domain.StudyReplyRepository;
import com.learnway.study.domain.StudyRepository;
import com.learnway.study.dto.StudyReplyDto;
import com.learnway.study.dto.StudyReplyResponseDto;

@Service
public class StudyReplyService {
	
	@Autowired
	private StudyReplyRepository studyReplyRepository;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private StudyRepository studyRepository;
	
	
	//댓글 생성
	public void addReply(StudyReplyDto dto,Principal principal) {
		Member member = memberRepository.findByMemberId(principal.getName())
	            .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + principal.getName()));
		
		
		StudyReply studyReply = StudyReply.builder().content(dto.getContent()).date(dto.getDate())
				               .study(studyRepository.findById(dto.getPostId()).get()).member(member).build();
		 
		studyReplyRepository.save(studyReply);
		
	}
	
	//해당게시글 댓글불러오기
	public List<StudyReplyResponseDto> replyList(StudyReplyDto dto)	{
		System.out.println(dto.getPostId() + "게시글 아이디");
		 List<StudyReply> list = studyReplyRepository.findByStudy_PostidOrderByDateDesc(dto.getPostId());
		 
		 List<StudyReplyResponseDto> response = list.stream()
		            .map(reply -> new StudyReplyResponseDto(reply.getStudy().getPostid(), reply.getMember().getMemberName(), reply.getContent(), reply.getDate()))
		            .collect(Collectors.toList());
		 
		return response;
	}
}
