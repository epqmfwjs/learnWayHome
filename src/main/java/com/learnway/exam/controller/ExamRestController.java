package com.learnway.exam.controller;

import com.learnway.exam.domain.Exam;
import com.learnway.exam.service.ExamService;
import com.learnway.member.service.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/exam")
public class ExamRestController {

    private final ExamService examService;

    
    /*
    * 시험 상세 정보 가져오기
    * */
    @GetMapping("/{examId}")
    public ResponseEntity<Exam> getExam(
            @PathVariable("examId") Long examId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){

        //getId
        Long memId = userDetails.getMemberId();

        Optional<Exam> exam = null;
        if(memId != null){
            exam = examService.findExamById(examId, memId);
        }
        if(exam.isPresent()){
            return new ResponseEntity<>(exam.get(), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
