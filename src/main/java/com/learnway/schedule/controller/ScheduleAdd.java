package com.learnway.schedule.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learnway.global.domain.Material;
import com.learnway.global.domain.MaterialRepository;
import com.learnway.global.domain.Studyway;
import com.learnway.global.domain.StudywayRepository;
import com.learnway.global.domain.Subject;
import com.learnway.global.domain.SubjectRepository;
import com.learnway.schedule.domain.DailyAchieveRepository;
import com.learnway.schedule.domain.ScheduleRepository;
import com.learnway.schedule.service.ScheduleService;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleAdd {
	
private static final Logger log = LoggerFactory.getLogger(ScheduleRestController.class);
	
	@Autowired
	private ScheduleService scheduleService;
	
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

    
    //학업구분,과목,학습종류 리스트로 만들어서 맵에 담기 
    @GetMapping("/options")
    public ResponseEntity<Map<String, List<Map<String, Object>>>> getOptions() {
        Map<String, List<Map<String, Object>>> options = new HashMap<>();
        
        List<Map<String, Object>> studyways = new ArrayList<>();
        for (Studyway studyway : studywayRepository.findAll()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", studyway.getStudywayCode());
            
            map.put("name", studyway.getStudyway());
            studyways.add(map);
        }
        options.put("studyways", studyways);
        
        List<Map<String, Object>> subjects = new ArrayList<>();
        for (Subject subject : subjectRepository.findAll()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", subject.getSubjectCode());
            map.put("name", subject.getSubject());
            subjects.add(map);
        }
        options.put("subjects", subjects);
        
        List<Map<String, Object>> materials = new ArrayList<>();
        for (Material material : materialRepository.findAll()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", material.getMaterialCode());
            map.put("name", material.getMaterial());
            materials.add(map);
        }
        options.put("materials", materials);
        
        return ResponseEntity.ok(options);
    }

}
