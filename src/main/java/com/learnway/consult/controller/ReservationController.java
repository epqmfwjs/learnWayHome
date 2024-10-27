package com.learnway.consult.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learnway.consult.domain.Consultant;
import com.learnway.consult.domain.ConsultantRepository;
import com.learnway.consult.domain.Memo;
import com.learnway.consult.domain.ReservationEntity;
import com.learnway.consult.dto.MemoRequest;
import com.learnway.consult.dto.ReservationDTO;
import com.learnway.consult.dto.ReservationRequest;
import com.learnway.consult.dto.UserInfoDTO;
import com.learnway.consult.service.ConsultantDetails;
import com.learnway.consult.service.ConsultantService;
import com.learnway.consult.service.ReservationService;
import com.learnway.member.domain.Member;
import com.learnway.member.service.CustomUserDetails;

@RestController
@RequestMapping("/api")
public class ReservationController {
	
    @Autowired
    private SseController sseController;
	
	@Autowired
    private ReservationService reservationService;
	
    @Autowired
    private ConsultantRepository consultantRepository;
    
    @Autowired
    private ConsultantService consultantService;
	
    // 상담사페이지 예약리스트
    @GetMapping("/reservationsList")
    public List<ReservationDTO> getReservationsList(@RequestParam("counselor_id") Long counselor_id,Authentication authentication) {
        Member member = null;
        if (authentication != null && authentication.isAuthenticated()) {
        	CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            member = userDetails.getMember();
        }
        System.out.println("멤버pk값 : " + member); 
    	
    	return reservationService.getReservationsListByConsultantId(member.getId());
    }
    
    //상담사예약페이지에서 상담신청자 정보 조회
    @GetMapping("/users/{userId}")
    public UserInfoDTO users(@PathVariable("userId") Long userId) {
    	System.out.println("유저조회 들어옴");
    	return reservationService.getUserInfo(userId);
    }
    
    // main에서 모달창에 상담사 불러오기위한 메서드
    @GetMapping("/consultants")
    public List<Consultant> getConsultants() {
        return reservationService.getAllConsultants();
    }
    
    // 특정상담사 정보가져오기(예약페이지요청)
    @GetMapping("/consultants/{consultantId}")
    public Optional<Consultant> getConsultants(@PathVariable("consultantId") Long consultantId) {
    	System.out.println("상담사정보조회들어옴");
        return reservationService.getConsultants(consultantId);
    }	
		
    
    // 모달창에서 넘어와 멤버개인의 예약일정 보여주기    
    @GetMapping("/myReservations")
    public List<ReservationEntity> getMyReservations(Authentication authentication) {
    	System.out.println("마이페이지에서 모달로 요청온 나의 상담리스트");
    	Member member = null;
        if (authentication != null && authentication.isAuthenticated()) {
        	CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            member = userDetails.getMember();
        }
        System.out.println("멤버pk값 : " + member.getMemberId()); 
    	
    	return reservationService.getReservationsByMemberId(member.getId());
    }
    
    // 모달창에서 넘어와 특정상담사 예약일정 보여주기    
    @GetMapping("/reservations")
    public List<ReservationEntity> getReservations(@RequestParam("consultant") Long consultant) {
    	System.out.println("특정상담사 예약일정");
    	System.out.println("특정상담사 : "+consultant);
    	return reservationService.getReservationsByConsultantId(consultant);
    }
    
    //예약취소메서드
    @DeleteMapping("/reservations/{deleteId}")
    public ResponseEntity<String> deleteReservations(@PathVariable("deleteId") Long deleteId,Authentication authentication) {
        System.out.println("deleteId : " + deleteId);
        Member member = null;
        Optional<ReservationEntity> deleteList = reservationService.findById(deleteId);
        if (authentication != null && authentication.isAuthenticated()) {
        	CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            member = userDetails.getMember();
        }
        String loginMemberId = member.getMemberId();
        String loginMemberPw = member.getMemberPw();
        String dbMemberId = deleteList.get().getMember().getMemberId();
        String dbMemberPw = deleteList.get().getMember().getMemberPw();
        
        System.out.println("로그인 아이디 :"+loginMemberId);
        System.out.println("예약자 아이디 :"+dbMemberId);
        
        if(loginMemberId.equals(dbMemberId) && loginMemberPw.equals(dbMemberPw)) {
            
        	reservationService.deleteById(deleteId);
            
            // 알림보내기
            // 예약 생성 후 알림 발송
            String name = deleteList.get().getMember().getMemberName();
            Long consultantId = deleteList.get().getCounselor().getId();
            LocalDateTime startTime = deleteList.get().getBookingStart();

            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendText(ChronoField.MONTH_OF_YEAR, TextStyle.FULL_STANDALONE)
                .appendLiteral(" ")
                .appendValue(ChronoField.DAY_OF_MONTH)
                .appendLiteral("일 ")
                .appendValue(ChronoField.HOUR_OF_DAY)
                .appendLiteral("시")
                .toFormatter(Locale.KOREAN);

            String formattedDate = startTime.format(formatter);
            
            String message = "취소알림"+"<br/>" + formattedDate + " " + name + " 님의" +"<br/>" +
                             "예약이 취소되었습니다.";
            System.out.println("3번 상담사아이디 표시 : " + consultantId);
            
            sseController.sendNotificationToConsultant(consultantId, message);
            
            return ResponseEntity.ok("예약이 성공적으로 취소되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("예약 취소 중 오류가 발생했습니다.");
        }
    }
    
    // 예약 추가하기
    @PostMapping("/reservations")
    public ResponseEntity<ReservationEntity> createReservation(@RequestBody ReservationRequest request, Authentication authentication) {
        System.out.println("request.getCounselor() : " + request.getCounselor());
        Optional<Consultant> optionalConsultant = consultantRepository.findById(request.getCounselor());
        if (!optionalConsultant.isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        Member member = null;
        if (authentication != null && authentication.isAuthenticated()) {
        	CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            member = userDetails.getMember();
        }
        Consultant consultant = optionalConsultant.get();
        ReservationEntity reservation = new ReservationEntity();
        reservation.setMember(member);
        reservation.setCounselor(consultant);
        reservation.setReservationContent(request.getReservationContent());
        reservation.setBookingStart(request.getBookingStart());
        reservation.setBookingEnd(request.getBookingEnd());

        ReservationEntity savedReservation = reservationService.save(reservation);
        
        // 알림보내기
        // 예약 생성 후 알림 발송
        LocalDateTime startTime = request.getBookingStart();
        LocalDateTime endTime = request.getBookingEnd();
        
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendText(ChronoField.MONTH_OF_YEAR, TextStyle.FULL_STANDALONE)
                .appendLiteral(" ")
                .appendValue(ChronoField.DAY_OF_MONTH)
                .appendLiteral("일 ")
                .appendValue(ChronoField.HOUR_OF_DAY)
                .appendLiteral("시")
                .toFormatter(Locale.KOREAN);

            String startFormatter = startTime.format(formatter);
            String endFormatter = endTime.format(formatter);
        
        String message = "신규 예약이 있습니다."+"<br/>" + "예약자 성함 : " + reservation.getMember().getMemberName() +"<br/>" +
                         "시작 시간 : " + startFormatter +"<br/>" +"종료 시간 : " + endFormatter;
        sseController.sendNotificationToConsultant(reservation.getCounselor().getId(), message);

        return ResponseEntity.ok(savedReservation);
    }
    //상담사예약페이지에서 상담신청자 정보 조회
    @GetMapping("/userinfo")
    public UserInfoDTO userinfo(Authentication authentication) {
    	System.out.println("유저조회 들어옴");
        Member member = null;
        if (authentication != null && authentication.isAuthenticated()) {
        	CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            member = userDetails.getMember();
        }
    	return reservationService.getUserInfo(member.getId());
    }
   
    //상담사페이지 사이드바 상담사정보 가져오기
    @GetMapping("/consultantInfo")
    public Optional<Consultant> consultantInfo(Authentication authentication) {
    	System.out.println("상담사조회 들어옴");
    	Consultant consultant = null;
    	if (authentication != null && authentication.isAuthenticated()) {
    		ConsultantDetails userDetails = (ConsultantDetails) authentication.getPrincipal();
    		consultant = userDetails.getConsultant();
    	}
    	System.out.println("상담사정보조회"+consultant.getId());
    	return reservationService.getConsultants(consultant.getId());
    }
    
    //메모 저장 요청
    @PostMapping("/memos")
    public ResponseEntity<String> saveMemo(Authentication authentication, @RequestBody MemoRequest memoRequest) {
    	Consultant consultant = null;
    	if (authentication != null && authentication.isAuthenticated()) {
    		ConsultantDetails userDetails = (ConsultantDetails) authentication.getPrincipal();
    		consultant = userDetails.getConsultant();
    	}
    	System.out.println("memoRequest.getMemoContents() : "+memoRequest.getMemoContents());
        Memo memo = consultantService.saveMemo(consultant.getId(), memoRequest.getMemoTitle(), memoRequest.getMemoContents());
        return ResponseEntity.status(HttpStatus.CREATED).body("메모저장 성공: " + memo.getMemoId());
    }
    
    //메모리스트 조회요청
    @GetMapping("/memos")
    public List<Memo> getMemo(Authentication authentication) {
    	System.out.println("메모리스트조회 들어옴");
    	Consultant consultant = null;
    	if (authentication != null && authentication.isAuthenticated()) {
    		ConsultantDetails userDetails = (ConsultantDetails) authentication.getPrincipal();
    		consultant = userDetails.getConsultant();
    	}
    	
        return consultantService.getMemoByConsultantId(consultant.getId());
    }
    
    //메모 디테일창 및 수정폼 요청
    @GetMapping("/memos/{memoId}")
    public List<Memo> getMemoDetail(@PathVariable("memoId") Long memoId) {
    	System.out.println("메모디테일 들어옴");
    	//List<Memo> list = consultantService.getMemoDetail(memoId);
    	return consultantService.getMemoDetail(memoId);
    }
    
    //메모 수정 요청
    @PutMapping("/memos/{memoId}")
    public ResponseEntity<String> MemoEdit(Authentication authentication,@PathVariable("memoId") Long memoId,@RequestBody MemoRequest memoRequest) {
    	System.out.println("메모수정 들어옴");
    	Consultant consultant = null;
    	if (authentication != null && authentication.isAuthenticated()) {
    		ConsultantDetails userDetails = (ConsultantDetails) authentication.getPrincipal();
    		consultant = userDetails.getConsultant();
    	}
    	Memo memo = consultantService.updateMemo(memoId,consultant.getId() ,memoRequest.getMemoTitle(), memoRequest.getMemoContents());
    	return ResponseEntity.status(HttpStatus.CREATED).body("메모업데이트 성공: " + memo.getMemoId());
    }
    
    //메모 삭제 요청
    @DeleteMapping("/memos/{memoId}")
    public void MemoDelete(@PathVariable("memoId") Long memoId ) {
    	System.out.println("메모삭제 들어옴");
    	consultantService.deleteBymemoId(memoId);
    }
}