package com.learnway.study.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnway.study.domain.Study;
import com.learnway.study.domain.StudyProblem;
import com.learnway.study.domain.StudyRepository;
import com.learnway.study.domain.StudyTag;
import com.learnway.study.domain.StudyTagRepository;
import com.learnway.study.dto.StudyTagDto;

@Service
public class StudyTagService {

    @Autowired
    private StudyTagRepository studyTagRepository;
    @Autowired
    private StudyRepository studyRepository;
    
    // 모든 태그 조회
    public List<StudyTag> findAllTag() {
        return studyTagRepository.findAll();
    }
    
    // 게시글 작성 시 태그 저장
    public void createTag(StudyTagDto studyTagDto, Study study) {
        List<String> tags = studyTagDto.getTag();
        if (tags != null && !tags.isEmpty()) {
            for (String tag : tags) {
                StudyTag studyTag = StudyTag.builder().tag(tag).study(study).build();
                studyTagRepository.save(studyTag);
            }
        }
    }
    
    // 게시글 태그 수정
    public void updateTag(StudyTagDto studyTagDto, int postId) {
        List<String> tags = studyTagDto.getTag();
        Study study = studyRepository.findByPostid(postId);
        if (tags != null && !tags.isEmpty()) {
            for (String tag : tags) {
                StudyTag studyTag = StudyTag.builder().tag(tag).study(study).build();
                studyTagRepository.save(studyTag);
            }
        }
    }
    
    // 게시글 태그 삭제
    public void deleteTag(int postId) {
        List<StudyTag> study = studyTagRepository.findByStudyPostid(postId);
        studyTagRepository.deleteAll(study);
    }
    
    // 게시글 조회 시 저장된 태그 값 read
    public List<StudyTag> findTag(int postid) {
        return studyTagRepository.findByStudyPostid(postid);
    }
    
    // 태그 검색
    public List<Integer> searchHashtags(StudyTagDto dto) {
        List<String> tags = dto.getTags();
        if (tags != null && !tags.isEmpty()) {
            List<Integer> postIds = studyTagRepository.findPostIdsByTags(tags, tags.size());
            // postId 값을 출력
            for (Integer postId : postIds) {
                System.out.println("Found postId: " + postId);
            }
            return postIds;
        }
        return List.of(); // 빈 리스트 반환
    }
}
