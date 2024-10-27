package com.learnway.exam.controller;

import com.learnway.exam.domain.Exam;
import com.learnway.exam.domain.Score;
import com.learnway.exam.dto.ScoreRequestDTO;
import com.learnway.exam.service.ExamService;
import com.learnway.global.domain.Subject;
import com.learnway.member.service.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/score")
public class ScoreRestController {

    private final ExamService examService;

    /*
    * 시험의 과목별 점수 목록 가져오기
    * */
    @GetMapping("/{examId}/{pageNo}")
    public ResponseEntity<Page<Score>> getScoreList(
            @PathVariable("pageNo") int pageNo,
            @PathVariable("examId") Long examId,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ) {
        //get memId
        Long memId = userDetails.getMemberId();

        Page<Score> page = examService.getScoreListByExam(examId, memId, PageRequest.of(pageNo-1,10));
        page.get().forEach(System.out::println);

        return new ResponseEntity<Page<Score>>(page, HttpStatus.OK);
    }
    
    /*
    * 과목 상세 불러오기
    * */
    @GetMapping("/exam/{scoreId}")
    public ResponseEntity<Score> getScore(@PathVariable long scoreId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        //get memId
        Long memId = userDetails.getMemberId();
        Optional<Score> score = examService.getScoreById(scoreId, memId);
        return score.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /*
    * 점수 등록
    * */
    @PostMapping("/{examId}")
    @Transactional
    public ResponseEntity<String> submitScores(
            @RequestBody List<ScoreRequestDTO> scoreRequestList,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        long memId = userDetails.getMemberId();
        try {
            scoreRequestList.forEach(x -> {
                Score score = Score.builder()
                    .scoreScore(x.getScoreScore())
                    .scoreExScore(x.getScoreExScore())
                    .scoreStdScore(x.getScoreStdScore())
                    .scoreGrade(x.getScoreGrade())
                    .exam(Exam.builder().examId(x.getExamId()).build())
                    .subject(Subject.builder().subjectCode(x.getSubjectCode()).build()).build();
                score.setMemId(memId);
                examService.writeScore(score,memId);
            });
            return ResponseEntity.ok("Scores submitted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    /*
    * 점수 수정
    * */
    @Transactional
    @PutMapping("/{examId}")
    public ResponseEntity<String> updateScores(
            @RequestBody List<ScoreRequestDTO> scoreRequestList,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        long memId = userDetails.getMemberId();
        // 점수 처리 로직 (예: 데이터베이스에 저장)
        try {
            scoreRequestList.forEach(x -> {
                Score score = Score.builder()
                        .scoreId(x.getScoreId())
                        .scoreScore(x.getScoreScore())
                        .scoreExScore(x.getScoreExScore())
                        .scoreStdScore(x.getScoreStdScore())
                        .scoreGrade(x.getScoreGrade())
                        .exam(Exam.builder().examId(x.getExamId()).build())
                        .subject(Subject.builder().subjectCode(x.getSubjectCode()).build()).build();
                score.setMemId(memId);
                if(!examService.updateScore(score,memId)) {
                    System.out.println("실패 scoreId = " + score.getScoreId());
                }
            });
            return ResponseEntity.ok("Scores submitted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }



    /*
    * 점수 삭제
    * */
    @DeleteMapping("/{examId}/{scoreId}")
    public ResponseEntity<Score> deleteScore(
            @PathVariable("examId") Long examId,
            @PathVariable("scoreId") Long scoreId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        //get memId
        Long memId = userDetails.getMemberId();
        System.out.println("memId = " + memId);

        if (scoreId == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            examService.deleteScore(memId, scoreId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

}
