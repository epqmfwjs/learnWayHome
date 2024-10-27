package com.learnway.schedule.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DailyAchieveDto {
	
	private Long DailyAchieveId;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;
	private double avgAchieveRate;
	private String memberId;
}
