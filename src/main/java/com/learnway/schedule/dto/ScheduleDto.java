package com.learnway.schedule.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.learnway.global.domain.Material;
import com.learnway.global.domain.Studyway;
import com.learnway.global.domain.Subject;
import com.learnway.member.domain.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDto {
	
	private Long scheduleId;
	
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime  startTime;
	
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime  endTime;
	private String studywayId;
	private String subjectId;
	private double scheduleAchieveRate;
	private List<ProgressDto> progresses;
	private Member member; 
	private List<Long> deletedProgressIds;
	
	@Override
	public String toString() {
		return "ScheduleDto [scheduleId=" + scheduleId + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", studywayId=" + studywayId + ", subjectId=" + subjectId + ", progresses=" + progresses + "]";
	}
	
	
	

}
