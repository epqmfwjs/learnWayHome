package com.learnway.exam.controller;

import com.learnway.exam.service.ExamService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@Controller
@RequestMapping("/score")
public class ScoreController {

    private final ExamService examService;


    @GetMapping("/")
    public String scorePage(Authentication authentication){
        if(authentication != null){
            return "exam/stats";
        }else {
            return "redirect:/";      }
    }
}
