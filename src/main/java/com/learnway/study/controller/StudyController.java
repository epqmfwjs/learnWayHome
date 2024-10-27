package com.learnway.study.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.learnway.study.domain.ChatRoom;
import com.learnway.study.domain.Study;
import com.learnway.study.domain.StudyProblemImg;
import com.learnway.study.domain.StudyRepository;
import com.learnway.study.domain.StudyTag;
import com.learnway.study.dto.ChatRoomDto;
import com.learnway.study.dto.StudyDto;
import com.learnway.study.dto.StudyProblemDto;
import com.learnway.study.dto.StudyProblemImgDto;
import com.learnway.study.dto.StudyTagDto;
import com.learnway.study.service.StudyChatService;
import com.learnway.study.service.StudyPostService;
import com.learnway.study.service.StudyProblemImgService;
import com.learnway.study.service.StudyProblemService;
import com.learnway.study.service.StudyService;
import com.learnway.study.service.StudyTagService;

@Controller
@RequestMapping
public class StudyController {
	
	
	@Autowired
	private StudyService studyService; 
	@Autowired
	private StudyRepository studyRepository;
	@Autowired
	private StudyPostService studyPostService;
	@Autowired
	private StudyTagService studyTagService;
	@Autowired
	private StudyProblemService studyProblemService; 
	@Autowired
	private StudyProblemImgService studyProblemImgService; 
	@Autowired
	private StudyChatService studyChatService;
	
	
	

//	@RequestMapping(value="/studylist",method= {RequestMethod.GET,RequestMethod.POST})
	@GetMapping("/studylist")
	public String studyList(@PageableDefault(size = 6) Pageable pageable, Model model) {
	    Page<Study> studies = studyPostService.getBoardList(pageable);

	    int startPage = Math.max(1, studies.getNumber() + 1 - 4);
	    int endPage = Math.min(studies.getNumber() + 1 + 4, studies.getTotalPages());

	    model.addAttribute("startPage", startPage);
	    model.addAttribute("endPage", endPage);
	    model.addAttribute("list", studies);

	    return "study/studylist";
	}
	
	
	@PostMapping("/studylist/search")
	public String studyListSearch(@PageableDefault(size = 7) Pageable pageable, Model model,
	                              @ModelAttribute StudyDto dto) {

	    if (dto.getDetailSearch() != null && !dto.getDetailSearch().isEmpty()) {
	        // String으로 받은 값을 ',' 기준으로 분할하여 int 배열로 변환
	        int[] detailSearchArray = Arrays.stream(dto.getDetailSearch().split(",")).mapToInt(Integer::parseInt).toArray();
	        // 변환된 배열을 dto에 설정
	        dto.setDetailSearchArray(detailSearchArray);
	        for(int a: detailSearchArray) {
	        	System.out.println(a + "중복된 값 출력");
	        }
	    }

	    Page<Study> studies = studyPostService.boardSearchList(dto, pageable);

	    if ((dto.getDetailSearch() == null || dto.getDetailSearch().isEmpty()) &&
	        (dto.getTitle() == null || dto.getTitle().isEmpty())) {
	        return "redirect:/studylist";
	    }

	    int startPage;
	    int endPage;

	    if (studies.getTotalPages() == 0) {
	        // 검색 결과가 없을 때 startPage와 endPage를 1로 설정
	        startPage = 1;
	        endPage = 1;
	        model.addAttribute("list", Page.empty(pageable));
	    } else {
	        startPage = Math.max(1, studies.getNumber() + 1 - 4);
	        endPage = Math.min(studies.getNumber() + 1 + 4, studies.getTotalPages());
	        model.addAttribute("list", studies);
	    }

	    model.addAttribute("startPage", startPage);
	    model.addAttribute("endPage", endPage);

	    return "study/studySearchList";
	}
	
	

	
	@GetMapping(value="/studyadd")
	public String studyAddView() {
		return "study/studyadd";
	}
	
	
	//수정 view메서드
	@PostMapping(value="/studyupdateview")
	public String studyUpdateView(StudyDto dto,Model model) {
	
		System.out.println(dto.getPostid()+ " 게시글id");
		Optional<Study> study =  studyService.updateView(dto);
		if(study.isPresent()) {
			model.addAttribute("study",study.get());
			model.addAttribute("content",study.get().getContent().replace("<br>","\n"));
			model.addAttribute("postid",dto.getPostid());
			return "study/studyupdate";
		}
		else {
			model.addAttribute("errmsg","게시글을 찾을 수 없습니다.");
			return "error/404";
		}
	}
	
	
	//게시글 추가 메서드
	@PostMapping(value="/studyadd")
	public String studyadd(StudyDto studyDto,ChatRoomDto chatRoomDto,StudyTagDto studyTagDto,
			StudyProblemDto studyProblemDto,StudyProblemImgDto studyProblemImgDto,
			@RequestParam("imgpath") MultipartFile[] files,Principal principal) {
		
		System.out.println("컨트롤러 접근");
		System.out.println(studyTagDto.getTag());
		System.out.println(files.length + " : 파일값");
		System.out.println(files.toString() + " : 파일String");
		studyService.crateBoard(studyDto,chatRoomDto,studyTagDto,studyProblemDto,studyProblemImgDto,files,principal);
		return "redirect:/studylist";
	}
	
	//게시글 수정 메서드
	@PostMapping(value="/studyupdate")
	public String studyUpdate(StudyDto studyDto,ChatRoomDto chatRoomDto,StudyTagDto studyTagDto,
			StudyProblemDto studyProblemDto,StudyProblemImgDto studyProblemImgDto,
			@RequestParam("imgpath") MultipartFile[] files,Principal principal) {
		
		System.out.println("게시글 id 수정창" + studyDto.getPostid());
		studyService.updateBoard(studyDto,chatRoomDto,studyTagDto,studyProblemDto,studyProblemImgDto,files,principal);
		
		return "redirect:studylist";
	}
	
	
	@GetMapping(value="/study/detail/"+"{postid}")
	public String studydetail(@PathVariable("postid") Integer postId,Model model,Principal principal) {
		Optional<Study> optionalStudy = studyRepository.findById(postId);
		List<StudyTag> tagList = studyTagService.findTag(postId);
		for(StudyTag a: tagList	) {
			System.out.println(a.getTag() + "태그값 ");
		}
		System.out.println(postId + " 게시글 번호");
		//포스트id로 문제id 조회
		int problemId = studyProblemService.problemId(postId);
		//postid로 ChatRoomId 조회
		List<ChatRoom> chatRoom = studyChatService.chatRoomId(postId);
		//problemId로 problemImgPathId조회
		List<StudyProblemImg> imgList = studyProblemImgService.problemImgPath(problemId);
		boolean result = studyService.boardCheck(postId, principal);
		
		if(optionalStudy.isPresent()) {
			Study study = optionalStudy.get();
			model.addAttribute("study",study);
			model.addAttribute("studyTag",tagList);
			model.addAttribute("imgList",imgList);
			model.addAttribute("chatRoom",chatRoom);
			model.addAttribute("hostList",result);
			model.addAttribute("member",principal.getName());
			System.out.println(" 값 전달 성공");
			return "study/studydetail";
		}else {
			model.addAttribute("errmsg","게시글을 찾을 수 없습니다.");
			return "error/404";
		}
		
		}
		

	
	
	

}
