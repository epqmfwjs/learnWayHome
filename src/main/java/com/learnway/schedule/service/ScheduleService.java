package com.learnway.schedule.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnway.global.domain.MaterialRepository;
import com.learnway.global.domain.StudywayRepository;
import com.learnway.global.domain.SubjectRepository;
import com.learnway.member.domain.Member;
import com.learnway.schedule.domain.DailyAchieve;
import com.learnway.schedule.domain.DailyAchieveRepository;
import com.learnway.schedule.domain.Progress;
import com.learnway.schedule.domain.ProgressRepository;
import com.learnway.schedule.domain.Schedule;
import com.learnway.schedule.domain.ScheduleRepository;
import com.learnway.schedule.dto.DailyAchieveDto;
import com.learnway.schedule.dto.ProgressDto;
import com.learnway.schedule.dto.ScheduleDto;

import jakarta.transaction.Transactional;

@Service
public class ScheduleService {
	
	@Autowired
	private ScheduleRepository scheduleRepository;
	
	@Autowired
	private DailyAchieveRepository dailyAchieveRepository;
	
	@Autowired
    private StudywayRepository studywayRepository;
    
    @Autowired
    private SubjectRepository subjectRepository;
    
    @Autowired
    private MaterialRepository materialRepository;
    
    @Autowired 
    private ProgressRepository progressRepository;

	    // 스케쥴 추가하기 (주간)
    	@Transactional
		public void add(ScheduleDto dto,Member member) {
    		
				Schedule schedule = new Schedule();
				schedule.setStartTime(dto.getStartTime());
				schedule.setEndTime(dto.getEndTime());
				schedule.setStudywayId(studywayRepository.findById(dto.getStudywayId())
				        .orElseThrow(() -> new RuntimeException("Studyway not found")));
				schedule.setSubjectId(subjectRepository.findById(dto.getSubjectId())
				        .orElseThrow(() -> new RuntimeException("Subject not found")));
				schedule.setMember(member);
				
				//Progress 엔티티 생성 및 설정
				List<Progress> progresses = new ArrayList<>();
				for (ProgressDto progressDto : dto.getProgresses()) {
		            Progress progress = new Progress();
		            progress.setMaterialId(materialRepository.findById(progressDto.getMaterialId())
		                    .orElseThrow(() -> new RuntimeException("Material not found")));	            
		            progress.setProgress(progressDto.getProgress());
		            progress.setScheduleId(schedule);
		            progress.setAchieveRate(progressDto.getAchieveRate());
		            progress.setMemberId(member.getMemberId());
		            progresses.add(progress);
		        }
				schedule.setProgresses(progresses);
				scheduleRepository.save(schedule);
		}
		
		//일정 전부 불러오기
		public List<Schedule> findAllByMemberId(Long memberId){
			return scheduleRepository.findAllByMemberId(memberId);
		}
		
		@Transactional
		//일정 시간만 변경하기 (주간)
		public void updateScheduleTime(ScheduleDto dto,Member member) {
			
			Schedule schedule = scheduleRepository.findById(dto.getScheduleId()).orElseThrow(() 
					-> new IllegalArgumentException("해당 일정이 존재하지 않습니다. id: " + dto.getScheduleId()));
		
	        
			schedule.setStartTime(dto.getStartTime());
			schedule.setEndTime(dto.getEndTime());
			schedule.setStudywayId(studywayRepository.findById(dto.getStudywayId())
			        .orElseThrow(() -> new RuntimeException("Studyway not found")));

			
			scheduleRepository.save(schedule);
		}
		
		//일일 스케쥴 가져오기
		public Optional<Schedule> getDetail(Long id, Long memberId){
				return scheduleRepository.findByScheduleIdAndMemberId(id,memberId);
			
		}
		
		@Transactional
		//일정 전부 변경하기 (주간)
		public void updateSchedule(ScheduleDto dto,Member member) {
			
			    Optional<Schedule> existingSchedule = scheduleRepository.findByScheduleIdAndMemberId(dto.getScheduleId(),member.getId());
			    if (existingSchedule.isPresent()) {
			        Schedule schedule = existingSchedule.get();
		
			        
			        // DTO의 값으로 schedule 객체 업데이트
			        schedule.setStartTime(dto.getStartTime());
			        schedule.setEndTime(dto.getEndTime());
			        schedule.setStudywayId(studywayRepository.findById(dto.getStudywayId())
					        .orElseThrow(() -> new RuntimeException("Studyway not found")));
					schedule.setSubjectId(subjectRepository.findById(dto.getSubjectId())
					        .orElseThrow(() -> new RuntimeException("Subject not found")));
					
					// 삭제된 Progress 처리
			        if (dto.getDeletedProgressIds() != null && !dto.getDeletedProgressIds().isEmpty()) {
			            for (Long deletedId : dto.getDeletedProgressIds()) {
			                progressRepository.deleteById(deletedId);
			            }
			        }
			        
					//Progress 엔티티 생성 및 설정
					List<Progress> progresses = new ArrayList<>();
					for (ProgressDto progressDto : dto.getProgresses()) {
			            Progress progress;
			            if (progressDto.getProgressId() != null) {
			                // 기존의 Progress 엔티티 조회
			                progress = progressRepository.findById(progressDto.getProgressId())
			                        .orElseThrow(() -> new RuntimeException("Progress not found"));
			            } else {
			                // 새로운 Progress 엔티티 생성
			                progress = new Progress();
			            }
			            progress.setMaterialId(materialRepository.findById(progressDto.getMaterialId())
			                    .orElseThrow(() -> new RuntimeException("Material not found")));
			            progress.setProgress(progressDto.getProgress());
			            progress.setScheduleId(schedule);
			            progress.setAchieveRate(progressDto.getAchieveRate());
			            progresses.add(progress);
			        }
					
					schedule.setProgresses(progresses);
					scheduleRepository.save(schedule);
		    
			}
		}
		
		//스케쥴 삭제하기
		@Transactional
		public void deleteSchedule(Long id,Long memberId) {
			// 연관된 Progress 엔티티 먼저 삭제
			Schedule schedule = scheduleRepository.findByScheduleIdAndMemberId(id,memberId)
		            .orElseThrow(() -> new IllegalArgumentException("해당 일정이 존재하지 않습니다. id: " + id));
		    
		    // 연관된 Progress 엔티티 먼저 삭제
		    progressRepository.deleteByScheduleId(schedule);
			scheduleRepository.deleteById(id);
		}
		
		//일달성율 평균 리스트 조회 
		public List<DailyAchieveDto> AchieveList(LocalDate start, LocalDate  end, Member member) {
		
			List<DailyAchieveDto> result = new ArrayList<>();
			
			LocalDate current = start;
	        while (current.isBefore(end)) {
	        	LocalDateTime currentDateTime = current.atTime(6, 0); // 각 날짜의 6시로 설정
	            DailyAchieve dailyAchieve = createDailyAchieve(currentDateTime,member.getId(),member);
	             
	            DailyAchieveDto dto = new DailyAchieveDto();
	            dto.setDailyAchieveId(dailyAchieve.getDailyAchieveId());
	            dto.setDate(dailyAchieve.getDate());
	            dto.setAvgAchieveRate(dailyAchieve.getAvgAchieveRate());
	            dto.setMemberId(member.getMemberId());
	            
	            result.add(dto);
	            current = current.plusDays(1);
	        }
	        
	        return result;
		
		}

		
		private DailyAchieve createDailyAchieve(LocalDateTime dateTime,Long memberId,Member member) {
			
			// LocalDateTime을 LocalDate로 변환
		    LocalDate date = dateTime.toLocalDate();
		    
		    // 해당 날짜에 대한 DailyAchieve 객체가 이미 존재하는지 확인
		    Optional<DailyAchieve> existingAchieve = dailyAchieveRepository.findByMemberIdAndDate(member.getMemberId(), date);

		    if (existingAchieve.isPresent()) {
		        // 이미 존재하는 경우, 기존 객체 업데이트
		        DailyAchieve dailyAchieve = existingAchieve.get();
		        dailyAchieve.setAvgAchieveRate(DailyAvgAchieve(dateTime, memberId));
		        return dailyAchieveRepository.save(dailyAchieve);
		    } else {
		        // 존재하지 않는 경우, 새로운 객체 생성
		        DailyAchieve newAchieve = new DailyAchieve();
		        newAchieve.setDate(date);
		        newAchieve.setAvgAchieveRate(DailyAvgAchieve(dateTime, memberId));
		        newAchieve.setMemberId(member.getMemberId());
		        return dailyAchieveRepository.save(newAchieve);
		    }
	    }
		
		//하루 평균 달성율 구하기
		public double DailyAvgAchieve(LocalDateTime dateTime,Long memberId) {
		    LocalDateTime endTime = dateTime.plusDays(1).minusSeconds(1); //;
		    List<Schedule> schedules = scheduleRepository.findByMemberIdAndStartTimeBetween(memberId,dateTime, endTime);
		    
		    if (schedules.isEmpty()) {
		        return 0.0;
		    }
		    
		    double totalAchieveRate = 0.0;
		    int totalProgresses = 0;
		    
		    for (Schedule schedule : schedules) {
		        totalAchieveRate += schedule.getScheduleAchieveRate();
		        totalProgresses++;
		    }
		    
		    if (totalProgresses == 0) {
		        return 0.0;
		    }
		    
		    double avgAchieveRate = totalAchieveRate / schedules.size();
		    return Math.round(avgAchieveRate * 10.0) / 10.0;
		}
		
		//한 일정 달성율 구하기
		public double scheduleAchieveRate(Long id) {
			Schedule schedule = scheduleRepository.findById(id).orElseThrow(() 
					-> new IllegalArgumentException("해당 일정이 존재하지 않습니다. id: " + id));
			
			List<Progress> progresses = schedule.getProgresses();
			
			if(progresses.isEmpty()) {
				return 0.0;
			}
			
			double totalAchieveRate = 0.0;
			int totalProgresses = 0;
			double scheduleAchieveRate = 0.0;
			
			for(Progress progress : progresses) {
				totalAchieveRate += progress.getAchieveRate();
				totalProgresses++;
			}
			scheduleAchieveRate = totalAchieveRate / progresses.size();
			scheduleAchieveRate = Math.round(scheduleAchieveRate * 10.0) / 10.0;
			
			ScheduleDto dto = new ScheduleDto();
			dto.setScheduleId(id);
			dto.setScheduleAchieveRate(scheduleAchieveRate); 
			schedule.setScheduleAchieveRate(scheduleAchieveRate);
			scheduleRepository.save(schedule);
	
			return scheduleAchieveRate;
		
		}
		
		
}
