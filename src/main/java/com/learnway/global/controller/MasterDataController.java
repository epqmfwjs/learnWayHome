package com.learnway.global.controller;

import com.learnway.global.domain.ExamT;
import com.learnway.global.domain.Material;
import com.learnway.global.domain.Studyway;
import com.learnway.global.domain.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.learnway.global.dto.MasterDataDTO;
import com.learnway.global.service.MasterDataService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

// 기준 정보(Material/Subject/StudyWay)를 관리하는 컨트롤러
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class MasterDataController {

    private final MasterDataService masterDataService;

    // 기준 정보 조회 페이지
    @GetMapping("/masterdata")
    public String showMasterDataPage(Model model) {
        return "admin/masterdata"; // 기준 정보 조회 페이지
    }

    // 기준 정보 추가 폼
    @GetMapping("/addMasterData")
    public String showForm(Model model, @ModelAttribute ("message") String message) {
        model.addAttribute("masterDataDTO", new MasterDataDTO());
        model.addAttribute("message", message);
        return "admin/addMasterData"; // 폼을 보여줄 뷰 이름
    }

    @PostMapping("/addMasterData")
    public String addMasterData(@Valid @ModelAttribute MasterDataDTO masterDataDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        // 유효성 검사 실패 시 입력된 데이터 Model 에 담아서 현재 페이지 반환
        if (bindingResult.hasErrors()) {
            model.addAttribute("masterDataDTO", masterDataDTO);
            return "admin/addMasterData";
        }
        // 중복 처리 예외
        try {
            masterDataService.addMasterData(masterDataDTO);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("masterDataDTO", masterDataDTO);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/addMasterData?error=true";
        }
        return "redirect:/admin/masterdata?message=success";
    }

    // 기준 정보 수정 폼
    @GetMapping("/updateMasterData/{category}/{code}")
    public String showUpdateForm(@PathVariable("category") String category, @PathVariable("code") String code, Model model) {
        MasterDataDTO masterDataDTO = masterDataService.getMasterDataByCategoryAndCode(category, code);
        model.addAttribute("masterDataDTO", masterDataDTO);
        return "admin/updateMasterData";
    }

    // 기준 정보 수정
    @PostMapping("/updateMasterData")
    public String updateMasterData(@Valid @ModelAttribute MasterDataDTO masterDataDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("masterDataDTO", masterDataDTO);
            return "admin/updateMasterData";
        }
        masterDataService.updateMasterData(masterDataDTO);
        return "redirect:/admin/masterdata?message=updateSuccess";
    }

    // 기준 정보 삭제
    @PostMapping("/deleteMasterData/{category}/{code}")
    public String deleteMasterData(@PathVariable("category") String category, @PathVariable("code") String code) {
        masterDataService.deleteMasterData(category, code);
        return "redirect:/admin/masterdata?message=deleteSuccess";
    }

    @GetMapping("/materials")
    @ResponseBody
    public Page<Material> getMaterials(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return masterDataService.getMaterials(pageable);
    }

    @GetMapping("/studyways")
    @ResponseBody
    public Page<Studyway> getStudyways(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return masterDataService.getStudyways(pageable);
    }

    @GetMapping("/subjects")
    @ResponseBody
    public Page<Subject> getSubjects(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return masterDataService.getSubjects(pageable);
    }

    @GetMapping("/exams")
    @ResponseBody
    public Page<ExamT> getExams(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return masterDataService.getExams(pageable);
    }

}
