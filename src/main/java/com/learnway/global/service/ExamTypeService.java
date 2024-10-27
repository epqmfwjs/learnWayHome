package com.learnway.global.service;

import com.learnway.global.domain.ExamType;
import com.learnway.global.domain.ExamTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ExamTypeService {

    ExamTypeRepository examTypeRepository;

    public List<ExamType> findAll() {
        return examTypeRepository.findAll();
    }

    public Optional<ExamType> findById(long id) {
        return examTypeRepository.findById(id);
    }

    public ExamType save(ExamType examType) {
        return examTypeRepository.save(examType);
    }

    public void deleteById(long id) {
        examTypeRepository.deleteById(id);
    }

    public ExamType update(ExamType examType) {
        Optional<ExamType> examTypeOptional = examTypeRepository.findById(examType.getExamTypeId());
        examTypeOptional.ifPresent(item -> {
            item.setExamTypeName(examType.getExamTypeName());
            examTypeRepository.save(item);
        });
        return examType;
    }

}
