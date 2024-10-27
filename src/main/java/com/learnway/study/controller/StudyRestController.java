package com.learnway.study.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learnway.study.domain.Study;
import com.learnway.study.dto.ChatRoomDto;
import com.learnway.study.dto.CorrectCheckDto;
import com.learnway.study.dto.StudyDto;
import com.learnway.study.dto.StudyReplyDto;
import com.learnway.study.dto.StudyReplyResponseDto;
import com.learnway.study.dto.StudyTagDto;
import com.learnway.study.service.StudyChatService;
import com.learnway.study.service.StudyCorrectService;
import com.learnway.study.service.StudyPostService;
import com.learnway.study.service.StudyReplyService;
import com.learnway.study.service.StudyService;
import com.learnway.study.service.StudyTagService;

@RestController
@RequestMapping("/api")
public class StudyRestController {

	@Autowired
	private StudyCorrectService studyCorrectService;
	@Autowired
	private StudyReplyService studyReplyService;
	@Autowired
	private StudyPostService studyPostService;
	@Autowired
	private StudyTagService studyTagService;
	@Autowired
	private StudyService studyService;
	@Autowired
	private StudyChatService studyChatService;
	
	
	@PostMapping("/member/correct")
	public boolean correct(@RequestBody CorrectCheckDto dto,Principal principal) {
		System.out.println("진입");
		System.out.println(dto.getStatus() + "상태값");
		System.out.println(dto.getRoomId() + "채팅방번호");
		System.out.println(dto.getPostId() + "게시글번호");
		
		studyCorrectService.updateStatus(dto,principal);
		return true;
	}
	
	
	@PostMapping("/study/delete")
	public void boardDelete(@RequestBody StudyDto dto,Principal principal) {
		
		studyPostService.boardDelete(dto,principal);
	}
	
	//댓글 추가 메서드
	@PostMapping("/member/replyadd")
	public List<StudyReplyResponseDto> replyAdd(@RequestBody StudyReplyDto dto,Principal principal) {
		
		
		
	        //댓글입력
	        studyReplyService.addReply(dto,principal);
	        
	        //추가된 댓글리스트 출력
	        List<StudyReplyResponseDto> list = studyReplyService.replyList(dto);
	        
	        return list;
	}
	
	// 댓글목록 리스트
	@PostMapping("/member/replylist")
	public List<StudyReplyResponseDto> replyList(@RequestBody StudyReplyDto dto,Principal principal) {
		
		
	        List<StudyReplyResponseDto> list = studyReplyService.replyList(dto);
	        
	        return list;
	}
	
	//검색 메서드
	@PostMapping("/study/searchList")
	public List<Study> searchBoardList(@RequestBody StudyDto dto) {
		
		System.out.println(dto.getTitle());
		return studyPostService.searchBoardList(dto);
	}
	
	
	//태그값 검색 메서드
	@PostMapping("/study/searchHashtags")
	public List<Integer> searchHashtags(@RequestBody StudyTagDto dto) {
		
		System.out.println(dto.getTags() + "태그값");
		
		return studyTagService.searchHashtags(dto);
	}
	
	
	//시작일 검색값 
	@PostMapping("/study/searchStartdate")
	public List<Integer> searchStartdate(@RequestBody StudyDto dto) {
		
		System.out.println(dto.getStartdate() + "시작일");
		return studyService.searchStartdate(dto);
	}
	
	//채팅방 게시글 값
	@PostMapping("/study/searchChatStudy")
	public List<Integer> searchChatStudy(@RequestBody ChatRoomDto dto) {
		
		System.out.println(dto.getRoomCheck() + " 채팅방 value값");
		return studyChatService.searchChatStudy(dto);
	}
	
	// 게시글 정답 체크메서드
	@PostMapping("/problemCheck")
	public boolean problemCheck(@RequestBody CorrectCheckDto dto,Principal principal) {
		
		return studyCorrectService.problemCheck(dto, principal);
		
	}
	
}
