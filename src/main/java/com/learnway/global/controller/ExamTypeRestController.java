package com.learnway.global.controller;

import com.learnway.global.domain.ExamType;
import com.learnway.global.service.ExamTypeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/examtype")
public class ExamTypeRestController {

    ExamTypeService examTypeService;

    @GetMapping("/all")
    public ResponseEntity<List<ExamType>> getAllExamTypes() {
        return new ResponseEntity<List<ExamType>>(examTypeService.findAll(), HttpStatus.OK);
    }
}
