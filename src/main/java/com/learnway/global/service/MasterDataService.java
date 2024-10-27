package com.learnway.global.service;

import com.learnway.global.domain.*;
import com.learnway.global.dto.MasterDataDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

// 기준 정보(Material/Subject/StudyWay)를 관리하는 서비스 클래스
@Service
@RequiredArgsConstructor
public class MasterDataService {

    private final MaterialRepository materialRepository;
    private final StudywayRepository studywayRepository;
    private final SubjectRepository subjectRepository;
    private final ExamTRepository examRepository;

    // 기준 정보 추가
    public void addMasterData(MasterDataDTO masterDataDTO) {
        switch (masterDataDTO.getCategory()) {
            // 학습 종류 선택 시
            case "material":
                if (materialRepository.existsByMaterialCode(masterDataDTO.getCode())) {
                    throw new IllegalArgumentException("이미 존재하는 학습 종류 코드입니다: " + masterDataDTO.getCode());
                }
                Material materialClass = Material.builder()
                        .materialCode(masterDataDTO.getCode())
                        .material(masterDataDTO.getName())
                        .materialNote(masterDataDTO.getNote())
                        .build();
                materialRepository.save(materialClass);
                System.out.println("학습 종류 추가 완료");
                break;
            // 학업 구분 선택 시
            case "studyway":
                if (studywayRepository.existsByStudywayCode(masterDataDTO.getCode())) {
                    throw new IllegalArgumentException("이미 존재하는 학업 구분 코드입니다: " + masterDataDTO.getCode());
                }
                Studyway studywayClass = Studyway.builder()
                        .studywayCode(masterDataDTO.getCode())
                        .studyway(masterDataDTO.getName())
                        .studywayNote(masterDataDTO.getNote())
                        .build();
                studywayRepository.save(studywayClass);
                System.out.println("학업 구분 추가 완료");
                break;
            // 과목 선택 시
            case "subject":
                if (subjectRepository.existsBySubjectCode(masterDataDTO.getCode())) {
                    throw new IllegalArgumentException("이미 존재하는 과목 코드입니다: " + masterDataDTO.getCode());
                }
                Subject subjectClass = Subject.builder()
                        .subjectCode(masterDataDTO.getCode())
                        .subject(masterDataDTO.getName())
                        .subjectNote(masterDataDTO.getNote())
                        .build();
                subjectRepository.save(subjectClass);
                System.out.println("과목 추가 완료");
                break;
            // 시험 유형 선택 시
            case "exam":
                if (examRepository.existsByExamCode(masterDataDTO.getCode())) {
                    throw new IllegalArgumentException("이미 존재하는 시험 유형 코드입니다: " + masterDataDTO.getCode());
                }
                ExamT examClass = ExamT.builder()
                        .examCode(masterDataDTO.getCode())
                        .exam(masterDataDTO.getName())
                        .examNote(masterDataDTO.getNote())
                        .build();
                examRepository.save(examClass);
                System.out.println("시험 종류 추가 완료");
                break;

            default: // IllegalArgumentException (잘못된 매개변수가 전달되었을 때 예외)
                throw new IllegalArgumentException("잘못된 카테고리입니다 : " + masterDataDTO.getCategory());
        }
    }
    // 기준 정보 수정
    public void updateMasterData(MasterDataDTO masterDataDTO) {
        switch (masterDataDTO.getCategory()) {
            case "material":
                Material material = materialRepository.findById(masterDataDTO.getCode()).orElseThrow(() -> new IllegalArgumentException("Invalid material code"));
                material = Material.builder()
                        .materialCode(material.getMaterialCode())
                        .material(masterDataDTO.getName())
                        .materialNote(masterDataDTO.getNote())
                        .build();
                materialRepository.save(material);
                break;
            case "studyway":
                Studyway studyway = studywayRepository.findById(masterDataDTO.getCode()).orElseThrow(() -> new IllegalArgumentException("Invalid studyway code"));
                studyway = Studyway.builder()
                        .studywayCode(studyway.getStudywayCode())
                        .studyway(masterDataDTO.getName())
                        .studywayNote(masterDataDTO.getNote())
                        .build();
                studywayRepository.save(studyway);
                break;
            case "subject":
                Subject subject = subjectRepository.findById(masterDataDTO.getCode()).orElseThrow(() -> new IllegalArgumentException("Invalid subject code"));
                subject = Subject.builder()
                        .subjectCode(subject.getSubjectCode())
                        .subject(masterDataDTO.getName())
                        .subjectNote(masterDataDTO.getNote())
                        .build();
                subjectRepository.save(subject);
                break;
            case "exam":
                ExamT exam = examRepository.findById(masterDataDTO.getCode()).orElseThrow(() -> new IllegalArgumentException("Invalid exam code"));
                exam = ExamT.builder()
                        .examCode(exam.getExamCode())
                        .exam(masterDataDTO.getName())
                        .examNote(masterDataDTO.getNote())
                        .build();
                examRepository.save(exam);
                break;
            default:
                throw new IllegalArgumentException("Invalid category");
        }
    }
    // 기준 정보 삭제
    public void deleteMasterData(String category, String code) {
        switch (category) {
            case "material" -> materialRepository.deleteById(code);
            case "studyway" -> studywayRepository.deleteById(code);
            case "subject" -> subjectRepository.deleteById(code);
            case "exam" -> examRepository.deleteById(code);
            default -> throw new IllegalArgumentException("Invalid category");
        }
    }

    // 해당 기준 정보 조회 (삭제, 조회 시)
    public MasterDataDTO getMasterDataByCategoryAndCode(String category, String code) {
        return switch (category) {
            case "material" -> {
                Material material = materialRepository.findById(code).orElseThrow(() -> new IllegalArgumentException("Material 없음"));
                yield new MasterDataDTO("material", material.getMaterialCode(), material.getMaterial(), material.getMaterialNote());
            }
            case "studyway" -> {
                Studyway studyway = studywayRepository.findById(code).orElseThrow(() -> new IllegalArgumentException("studyway 없음"));
                yield new MasterDataDTO("studyway", studyway.getStudywayCode(), studyway.getStudyway(), studyway.getStudywayNote());
            }
            case "subject" -> {
                Subject subject = subjectRepository.findById(code).orElseThrow(() -> new IllegalArgumentException("subject 없음"));
                yield new MasterDataDTO("subject", subject.getSubjectCode(), subject.getSubject(), subject.getSubjectNote());
            }
            case "exam" -> {
                ExamT exam = examRepository.findById(code).orElseThrow(() -> new IllegalArgumentException("exam 없음"));
                yield new MasterDataDTO("exam", exam.getExamCode(), exam.getExam(), exam.getExamNote());
            }
            default -> throw new IllegalArgumentException("카테고리를 찾을 수 없습니다");
        };
    }

    public Page<Material> getMaterials(Pageable pageable) {
        return materialRepository.findAll(pageable);
    }

    public Page<Studyway> getStudyways(Pageable pageable) {
        return studywayRepository.findAll(pageable);
    }

    public Page<Subject> getSubjects(Pageable pageable) {
        return subjectRepository.findAll(pageable);
    }

    public Page<ExamT> getExams(Pageable pageable) {
        return examRepository.findAll(pageable);
    }

    public List<Subject> getSubjects() {
        return subjectRepository.findAll();
    }
}
