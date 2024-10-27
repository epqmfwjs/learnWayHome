package com.learnway.study.service;

import java.security.Principal;
import java.sql.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.learnway.member.domain.Member;
import com.learnway.member.domain.MemberRepository;
import com.learnway.study.domain.Study;
import com.learnway.study.domain.StudyRepository;
import com.learnway.study.domain.StudyTagRepository;
import com.learnway.study.dto.ChatRoomDto;
import com.learnway.study.dto.StudyDto;
import com.learnway.study.dto.StudyProblemDto;
import com.learnway.study.dto.StudyProblemImgDto;
import com.learnway.study.dto.StudyTagDto;



//스터디게시글 트랜젝션 처리 서비스
@Service
public class StudyService {

	
	private StudyPostService studyPostService;
	private StudyChatService studyChatService;
	private StudyTagService studyTagService;
	private StudyProblemService studyProblemService;
	private StudyProblemImgService studyProblemImgService;
	private MemberRepository memberRepository;
	private StudyRepository studyRepository;  
	private StudyTagRepository studyTagRepository;  
	@Autowired
	public StudyService(StudyPostService studyPostService, StudyChatService studyChatService,
						StudyTagService studyTagService,StudyProblemService studyProblemService
						,StudyProblemImgService studyProblemImgService,MemberRepository memberRepository
						,StudyRepository studyRepository,StudyTagRepository studyTagRepository) {
		this.studyPostService = studyPostService;
		this.studyChatService = studyChatService;
		this.studyTagService = studyTagService;
		this.studyProblemService = studyProblemService;
		this.studyProblemImgService = studyProblemImgService;
		this.memberRepository = memberRepository;
		this.studyRepository = studyRepository;
		this.studyTagRepository = studyTagRepository;
	}
	
	
	
	// 게시글 생성 메서드 
	@Transactional
	public void crateBoard(StudyDto dto,ChatRoomDto chatRoomDto,StudyTagDto studyTagDto,
						StudyProblemDto studyProblemDto,StudyProblemImgDto studyProblemImgDto
						,MultipartFile[] files,Principal principal) {
		
		System.out.println("게시글생성");
		//게시글 생성
		Study study = studyPostService.boardadd(dto,principal);
		int postid = study.getPostid();
		
		//채팅방 생성
		if(chatRoomDto.getRoomname() !=null && !chatRoomDto.getRoomname().isEmpty()) {
			System.out.println(chatRoomDto.getName());
			System.out.println(chatRoomDto.getRoomname());
			System.out.println("--채팅방생성--");
		studyChatService.chatRoomCreate(chatRoomDto, study,principal);
		
		//studyChatService.joinChatRoom(chatRoomDto, principal);
		}
		//태그값 저장
		studyTagService.createTag(studyTagDto, study);
		//문제 저장
		int problemid = studyProblemService.problemAdd(studyProblemDto,postid,principal);
		
		System.out.println(problemid + "해당문제아이디");
		
		//문제이미지 저장
		studyProblemImgService.problemImgAdd(studyProblemImgDto,files,problemid);
	}

	//게시글 수정view 데이터조회
	public Optional<Study> updateView(StudyDto dto) {
		return studyRepository.findById(dto.getPostid());
		
	}
	
	
	//게시글 수정 메서드
	@Transactional
	public void updateBoard(StudyDto dto,ChatRoomDto chatRoomDto,StudyTagDto studyTagDto,
			StudyProblemDto studyProblemDto,StudyProblemImgDto studyProblemImgDto
			,MultipartFile[] files,Principal principal) {
		//게시글 수정
		Study study = studyPostService.boardUpdate(dto,principal);
		int postid = study.getPostid();
		
		//채팅방 제목 수정 (수정중)
		if(chatRoomDto.getRoomname() !=null && !chatRoomDto.getRoomname().isEmpty()) {
			System.out.println(chatRoomDto.getName());
			System.out.println(chatRoomDto.getRoomname());
			System.out.println("--채팅방생성--");
			studyChatService.chatRoomUpdate(chatRoomDto, study,principal,postid);
		}
		//태그값 수정
		if(studyTagRepository.findByStudyPostid(postid) !=null) {
		studyTagService.updateTag(studyTagDto,postid);
		}
		//문제 수정
		Integer problemid = studyProblemService.problemUpdate(studyProblemDto,postid,principal);
		System.out.println(problemid + ": 문제아이디");
		if(problemid != null) {
			//문제이미지 저장
			studyProblemImgService.problemImgUpdate(studyProblemImgDto,files,problemid);
		}
	}
	
	
//	게시글 작성자 확인 메서드
//  postId에 대한 member_id와 principal의 member_id 값일치 확인
	public boolean boardCheck(int postId, Principal principal) {
	    // Get member information based on principal
	    Member member = memberRepository.findByMemberId(principal.getName())
	        .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + principal.getName()));
	    
	    int memberId = member.getId().intValue();
	    
	    // Find the study by postId
	    Optional<Study> optionalStudy = studyRepository.findById(postId);
	    
	    if (optionalStudy.isPresent()) {
	        Study study = optionalStudy.get();
	        int postMemberId = study.getMember().getId().intValue();
	        
	        return memberId == postMemberId;
	    } else {
	        throw new NoSuchElementException("Study not found for postId: " + postId);
	    }
	}


	//시작일 게시글 조회
	public List<Integer> searchStartdate(StudyDto dto) {
	        Date startDate = dto.getStartdate(); // Date 객체 그대로 사용
	        List<Study> studies = studyRepository.findByStartdateGreaterThanEqual(startDate);
	        
	        for(Study a : studies) {
	        	System.out.println(a.getPostid() + "게시글아이디");
	        }
	        
	        List<Integer> postIds = studies.stream()
                    .map(Study::getPostid)
                    .collect(Collectors.toList());
	        return postIds;
	}

	
}

