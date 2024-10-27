package com.learnway.exam.controller;

import com.learnway.exam.domain.Exam;
import com.learnway.exam.domain.Score;
import com.learnway.exam.service.ExamService;
import com.learnway.member.service.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.http.GET;

import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/stats")
public class StatsRestController {

    private final ExamService examService;

    //과목별, 년도별 성적
    @GetMapping("/{examTypeName}/{year}")
    public ResponseEntity<List<Exam>> getExamsByExamTypeAndYear(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("examTypeName") String examTypeName,
            @PathVariable("year") int year
    ) {
        Long memId = userDetails.getMemberId();
        List<Exam> list = examService.findExamByExamTypeAndYear(memId, examTypeName, year);
        if(list != null){
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    };

    //과목별, 기간별
    @GetMapping("/{examTypeName}/{startDate}/{endDate}")
    public ResponseEntity<List<Exam>> getExamsByExamTypeAndDate(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("examTypeName") String examTypeName,
            @PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate
            ) {
        Long memId = userDetails.getMemberId();
        List<Exam> list = examService.findExamByExamTypeAndDate(memId, examTypeName, startDate, endDate);
        if(list != null){
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    };

    //기간별 성적
    @GetMapping("/all/{startDate}/{endDate}")
    public ResponseEntity<List<Exam>> getExamsByExamTypeAndYear(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate
    ) {
        Long memId = userDetails.getMemberId();
        List<Exam> list = examService.findExamByDate(memId, startDate, endDate);
        if(list != null){
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    };


    //년도별 성적
    @GetMapping("/all/{year}")
    public ResponseEntity<List<Exam>> getExamsByExamTypeAndYear(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("year") int year
    ) {
        Long memId = userDetails.getMemberId();
        List<Exam> list = examService.findExamByYear(memId, year);
        if(list != null){
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    };

    //전체 성적
    @GetMapping("/all/all")
    public ResponseEntity<List<Exam>> getExams(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memId = userDetails.getMemberId();
        List<Exam> list = examService.findAllExam(memId);
        if(list != null){
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    };



    //과목별 성적
    @GetMapping("/admin/subject/{subjectCode}/{pageNo}")
    public ResponseEntity<Page<Score>> getScoreBySubjectAdmin(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("subjectCode") String subjectCode,
            @PathVariable("pageNo") int pageNo
    ) {
        Long memId = userDetails.getMemberId();


        Page<Score> page = examService.getScoreListByExamType(memId, subjectCode, PageRequest.of(pageNo-1, 10));
        if(page != null){
            return new ResponseEntity<>(page, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    //시험 유형별 성적 추이
    @GetMapping("/admin/{examTypeName}/{pageNo}")
    public ResponseEntity<Page> getScoreByExamTypeAdmin(
            @PathVariable("examTypeName") String examTypeName,
            @PathVariable("pageNo") int pageNo,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        Long memId = userDetails.getMemberId();

        Page<Exam> page;
        if(!examTypeName.equals("all")){
            page = examService.findScoreListByExamType(memId, examTypeName, PageRequest.of(pageNo-1, 5));
        } else {
            page = examService.findScoreList(memId, PageRequest.of(pageNo-1, 5));
        }
        if(page != null){
            return new ResponseEntity<>(page, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
