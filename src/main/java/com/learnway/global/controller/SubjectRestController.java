package com.learnway.global.controller;

import com.learnway.global.domain.Subject;
import com.learnway.global.service.MasterDataService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/subject")
public class SubjectRestController {

    private final MasterDataService masterDataService;

    @GetMapping("/")
    public ResponseEntity<List<Subject>> getSubjects(){
        List<Subject> subjects = masterDataService.getSubjects();
        if(subjects == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(subjects, HttpStatus.OK);
        }
    }
}
