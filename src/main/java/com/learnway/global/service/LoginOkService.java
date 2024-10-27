package com.learnway.global.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnway.member.domain.Member;
import com.learnway.schedule.domain.DailyAchieve;
import com.learnway.schedule.domain.Schedule;
import com.learnway.schedule.domain.ScheduleRepository;
import com.learnway.schedule.dto.DailyAchieveDto;
import com.learnway.schedule.dto.ScheduleDto;

@Service
public class LoginOkService {
	
	@Autowired
	private ScheduleRepository scheduleRepository;

	public List<ScheduleDto> scheduleList(LocalDate start, LocalDate end, Member member) {
		
		List<ScheduleDto> result = new ArrayList<>();
		LocalDate current = start;
        while (current.isBefore(end)) {
        	LocalDateTime startTime = current.atTime(6, 0); // 각 날짜의 6시로 설정
        	LocalDateTime endTime = startTime.plusDays(1).minusSeconds(1);
        	Schedule schedule = scheduleRepository.findFirstByMemberIdAndStartTimeBetween(member.getId(),startTime, endTime);
            
            ScheduleDto dto = new ScheduleDto();
            if (schedule != null) {
                dto.setScheduleId(schedule.getScheduleId());
            }
            dto.setStartTime(startTime);
            
            result.add(dto);
            current = current.plusDays(1);
        }
		
		return result;
	}

}
