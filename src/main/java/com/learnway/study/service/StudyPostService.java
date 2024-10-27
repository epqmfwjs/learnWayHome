package com.learnway.study.service;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learnway.member.domain.MemberRepository;
import com.learnway.study.domain.Study;
import com.learnway.study.domain.StudyProblemImgRepository;
import com.learnway.study.domain.StudyProblemRepository;
import com.learnway.study.domain.StudyRepository;
import com.learnway.study.dto.StudyDto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class StudyPostService {
	
	@Autowired
	private StudyRepository studyRepository;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private StudyProblemRepository studyProblemRepository;
	@Autowired
	private StudyProblemImgRepository studyProblemImgRepository;
	@PersistenceContext
	private EntityManager entityManager;
	
	//모든게시글 출력
	public List<Study> findAll() {
        return studyRepository.findAll(Sort.by(Sort.Direction.DESC,"postid"));
    }

	//게시글 전체검색 메서드
	public Page<Study> getBoardList(Pageable pageable) {
        Sort sort = Sort.by(Sort.Direction.DESC, "postid");
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<Study> studies = studyRepository.findAll(sortedPageable);

        return studies;
    }
	//게시글 상세검색 메서드
	public Page<Study> boardSearchList(StudyDto dto, Pageable pageable) {
        // title과 detail 배열을 가져옴
        String title = dto.getTitle();
        int[] detail = dto.getDetailSearchArray();
        System.out.println(dto.getTitle() + "제목값");
        System.out.println(dto.getDetailSearchArray() + " 배열길이");
        // title과 detail이 둘 다 null이 아닐 때
        boolean hasTitle = title != null && !title.isEmpty();
        boolean hasDetail = detail != null && detail.length > 0;
        
        if (hasTitle && hasDetail) {
            List<Study> list = studyRepository.findByTitleContaining(title);

            // Study 리스트에서 postid 값만 추출
            List<Integer> postIds = list.stream()
                                        .map(Study::getPostid)
                                        .collect(Collectors.toList());

            // detail 배열과 postIds 리스트의 중복값 찾기
            List<Integer> duplicates = IntStream.of(detail)
                                                .boxed()
                                                .filter(postIds::contains)
                                                .collect(Collectors.toList());

            // 중복값 출력 (디버깅 또는 로깅용)
            System.out.println("중복된 값: " + duplicates);

            // 중복된 postid를 가진 Study 엔티티 페이징 처리하여 반환
            if (duplicates.isEmpty()) {
                return Page.empty(pageable);
            } else {
                Sort sort = Sort.by(Sort.Direction.DESC, "postid");
                Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
                return studyRepository.findByPostidIn(duplicates, sortedPageable);
            }
        }

        // title만 있을 때
        if (hasTitle) {
        	System.out.println("제목값만 들어옴");
            Sort sort = Sort.by(Sort.Direction.DESC, "postid");
            Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
            return studyRepository.findByTitleContaining(title, sortedPageable);
        }

        // detail 배열만 있을 때
        if (hasDetail) {
            Sort sort = Sort.by(Sort.Direction.DESC, "postid");
            Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
            return studyRepository.findByPostidIn(detail, sortedPageable);
        }

        // 둘 다 null일 때는 빈 페이지 반환
        return Page.empty(pageable);
    }
	
	//게시글 작성(게시글,지도,스터디채팅방,태그,문제 트랜젝션처리)
	//현재메서드는 게시글작성 및  return 값으로는 작성중인 potsId값 반환
	public Study boardadd(StudyDto dto,Principal principal) {
		
		 Date startdate=parseStringToSqlDate(dto.getStartdatetest());
		 Date enddate=parseStringToSqlDate(dto.getEnddatetest());
		
		 dto.setStartdate(startdate);
		 dto.setEnddate(enddate);
		 
		if(dto.getStartdatetest()!=null ||!dto.getStartdatetest().isEmpty() &&
				dto.getEnddatetest()!=null ||!dto.getEnddatetest().isEmpty()) {
		Study study = Study.builder().title(dto.getTitle())
									       .content(dto.getContent().replace("\n", "<br>"))
									       .viewcount("0")
									       .startdate(dto.getStartdate())
									       .enddate(dto.getEnddate())
									       .isjoin((byte) dto.getIsjoin()).
									       member(memberRepository.findByMemberId(principal.getName()).get()).build();
		return studyRepository.save(study);
		}
		Study study = Study.builder().title(dto.getTitle())
			       .content(dto.getContent())
			       .viewcount("0")
			       .isjoin((byte) dto.getIsjoin()).
			       member(memberRepository.findByMemberId(principal.getName()).get()).build();
	    
		return studyRepository.save(study);
		
		
	}
	  private Date parseStringToSqlDate(String dateString) {
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        Date date = null;
	        try {
	            java.util.Date utilDate = sdf.parse(dateString);
	            date = new Date(utilDate.getTime());
	        } catch (ParseException e) {
	            e.printStackTrace(); // 예외 처리 필요
	        }
	        return date;
	    }
	  
	// 게시글 수정 메서드
	public Study boardUpdate(StudyDto dto,Principal principal) {
		
		Study study = Study.builder().postid(dto.getPostid()).title(dto.getTitle())
				.content(dto.getContent().replace("\n", "<br>"))
				.viewcount(dto.getViewcount())
				.startdate(dto.getStartdate())
				.enddate(dto.getEnddate())
				.isjoin((byte) dto.getIsjoin()).member(memberRepository.findByMemberId(principal.getName()).get()).build();
		
		
		return studyRepository.save(study);
		
		
	}
	
	
	
	//게시글 제목검색 메서드
	public List<Study> searchBoardList(StudyDto dto) {
		return studyRepository.findByTitleContaining(dto.getTitle());
	}
	
	
	@Transactional
	public void boardDelete(StudyDto dto, Principal principal) {
	    // 자식 테이블 데이터 먼저 삭제
	    entityManager.createNativeQuery("DELETE FROM study_reply WHERE study_postid = :postid")
	            .setParameter("postid", dto.getPostid())
	            .executeUpdate();
	    entityManager.createNativeQuery("DELETE FROM study_tag WHERE study_postid = :postid")
	            .setParameter("postid", dto.getPostid())
	            .executeUpdate();
	    entityManager.createNativeQuery("DELETE FROM correct_check WHERE study_postid = :postid")
	            .setParameter("postid", dto.getPostid())
	            .executeUpdate();
	    entityManager.createNativeQuery("DELETE FROM problems_img WHERE study_problemid IN (SELECT study_problemid FROM problems WHERE study_postid = :studyId)")
        .setParameter("studyId", dto.getPostid())
        .executeUpdate();
	    entityManager.createNativeQuery("DELETE FROM problems WHERE study_postid = :postid")
	            .setParameter("postid", dto.getPostid())
	            .executeUpdate();
	    
	    entityManager.createNativeQuery("DELETE FROM chat_message WHERE study_chatroomid IN (SELECT study_chatroomid FROM study_chatroom WHERE study_postid = :postid)")
	    .setParameter("postid", dto.getPostid())
	    .executeUpdate();
	    
	    entityManager.createNativeQuery("DELETE FROM chatroommember WHERE study_chatroomid IN (SELECT study_chatroomid FROM study_chatroom WHERE study_postid = :postid)")
	    .setParameter("postid", dto.getPostid())
	    .executeUpdate();
	    
	    
	    
	    entityManager.createNativeQuery("DELETE FROM study_chatroom WHERE study_postid = :postid")
	    .setParameter("postid", dto.getPostid())
	    .executeUpdate();

	    // Study 테이블 데이터 삭제
	    entityManager.createNativeQuery("DELETE FROM study WHERE study_postid = :postid")
	            .setParameter("postid", dto.getPostid())
	            .executeUpdate();
	}
}
