package com.learnway.global.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.learnway.global.service.LoginOkService;
import com.learnway.member.domain.Member;
import com.learnway.member.service.CustomUserDetails;
import com.learnway.notice.domain.Notice;
import com.learnway.notice.service.NoticeService;
import com.learnway.schedule.dto.DailyAchieveDto;
import com.learnway.schedule.dto.ScheduleDto;
import com.learnway.schedule.service.ScheduleService;

@Controller
public class LoginOkController {
	
	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private LoginOkService loginOkService;
	
	// 시큐리티 로그인 성공 후 Redirect 경로 Get 매핑 -> 하기 메서드는 로그인 후 홈이 설정되면 삭제될 예정
    @GetMapping("/loginOk")
    public String loginOK(Model model,@RequestParam(value="page", defaultValue="0") int page) {
    	
    	//공지사항 부분
		Page<Notice> priNotice;
		Pageable pri = PageRequest.of(page, 8);
		priNotice = noticeService.priNoticeList(pri);
		System.out.println(priNotice);
		model.addAttribute("priNotice",priNotice);
    			
    			
        return "loginOk";
    }
    
    @ResponseBody
    @GetMapping("/login/getMonthlySchedule")
    public ResponseEntity<List<ScheduleDto>> getMonthlySchedule(@RequestParam("year") int year, @RequestParam("month") int month
			,Authentication authentication){
    	Member member = null;
        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            member = user.getMember();
            
        }
        LocalDate start = LocalDate.of(year, month, 1);
	    LocalDate end = start.plusMonths(1);
	    
	    List<ScheduleDto> schedules = loginOkService.scheduleList(start,end,member);
        return ResponseEntity.ok(schedules);
    }

}
