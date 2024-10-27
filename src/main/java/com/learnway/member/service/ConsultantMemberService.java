package com.learnway.member.service;

import com.learnway.consult.domain.Consultant;
import com.learnway.global.exceptions.S3Exception;
import com.learnway.global.service.S3ImageService;
import com.learnway.member.dto.ConsultantJoinDTO;
import com.learnway.consult.domain.ConsultantRepository;
import com.learnway.member.domain.MemberRepository;
import com.learnway.member.dto.ConsultantUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class ConsultantMemberService {

    private final ConsultantRepository consultantRepository;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final S3ImageService s3ImageService;

    private final String imgDir = "images/consultant";
    private static final String DEFAULT_IMAGE_PATH = "/img/member/member-default.png";

    public void joinConsultant(ConsultantJoinDTO consultantJoinDTO) {
        // member / consultant 테이블에서 동일한 ID 조회
        if (consultantRepository.findByConsultantId(consultantJoinDTO.getUsername()).isPresent() ||
                memberRepository.findByMemberId(consultantJoinDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 ID입니다.");
        }
        // 패스워드, 패스워드 컨펌 일치 확인
        if (!consultantJoinDTO.getPassword().equals(consultantJoinDTO.getConfirmPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 이미지 저장
        String imagePath;
        try {
            if(consultantJoinDTO.getImage() != null&& !consultantJoinDTO.getImage().isEmpty()){
                imagePath = s3ImageService.upload(consultantJoinDTO.getImage(),imgDir);
            } else {
                // 이미지가 없을 경우 기본 이미지 경로 설정
                imagePath = DEFAULT_IMAGE_PATH;
            }
        } catch (S3Exception e) {
            throw new IllegalStateException("이미지 저장에 실패했습니다.",e);
        }

        Consultant consultant = Consultant.builder()
                .consultantId(consultantJoinDTO.getUsername())
                .password(bCryptPasswordEncoder.encode(consultantJoinDTO.getPassword()))
                .name(consultantJoinDTO.getName())
                .subject(consultantJoinDTO.getSubject())
                .description(consultantJoinDTO.getDescription())
                .imageUrl(imagePath)
                .role("COUNSELOR")
                .build();

        consultantRepository.save(consultant);
    }

    // 컨설턴트 정보 조회
    public ConsultantUpdateDTO getConsultantById(Long id) {
        Consultant consultant = consultantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 컨설턴트입니다."));
        ConsultantUpdateDTO consultantUpdateDTO = new ConsultantUpdateDTO();
        consultantUpdateDTO.setUsername(consultant.getConsultantId());
        consultantUpdateDTO.setName(consultant.getName());
        consultantUpdateDTO.setSubject(consultant.getSubject());
        consultantUpdateDTO.setDescription(consultant.getDescription());
        consultantUpdateDTO.setImageUrl(consultant.getImageUrl());
        return consultantUpdateDTO;
    }

    // 컨설턴트 정보 수정
    public void updateConsultant(ConsultantUpdateDTO consultantUpdateDTO) {

        Consultant consultant = consultantRepository.findByConsultantId(consultantUpdateDTO.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 컨설턴트입니다."));
        //패스워드 컨펌과 패스워드 비교
        if (consultantUpdateDTO.getPassword() != null && !consultantUpdateDTO.getPassword().isEmpty()) {
            consultant.setPassword(bCryptPasswordEncoder.encode(consultantUpdateDTO.getPassword()));
        }

        String imagePath = consultant.getImageUrl(); // 기존 이미지 경로 저장
        try {
            if (consultantUpdateDTO.getImage() != null && !consultantUpdateDTO.getImage().isEmpty()) {
                // 새로운 이미지 업로드
                String newImagePath = s3ImageService.upload(consultantUpdateDTO.getImage(), imgDir);
                // 기본 이미지가 아닌 경우에만 기존 이미지 삭제
                if (!DEFAULT_IMAGE_PATH.equals(imagePath)) {
                    s3ImageService.deleteImageFromS3(imagePath);
                }
                imagePath = newImagePath; // 새 이미지 경로 설정
            } else if (imagePath == null || imagePath.isEmpty()) {
                // 이미지가 없을 경우 기본 이미지 경로 설정
                imagePath = DEFAULT_IMAGE_PATH;
            }
        } catch (S3Exception e) {
            throw new IllegalStateException("이미지 저장에 실패했습니다.", e);
        }

        consultant.setName(consultantUpdateDTO.getName());
        consultant.setSubject(consultantUpdateDTO.getSubject());
        consultant.setDescription(consultantUpdateDTO.getDescription());
        consultant.setImageUrl(imagePath);

        consultantRepository.save(consultant);
    }

//    private String saveImage(MultipartFile image) throws IOException {
//        if (image == null || image.isEmpty()) {
//            return "/img/member/member-default.png"; // 기본 이미지 경로
//        }
//        // 이름 위에 현재 시간 작성하여 파일 구분
//        String filename = System.currentTimeMillis() + "_" + image.getOriginalFilename();
//        Path imagePath = Paths.get(uploadPath, filename);
//        Files.createDirectories(imagePath.getParent());
//        Files.copy(image.getInputStream(), imagePath);
//
//        return filename;
//    }

    // 이미지 삭제 메서드
//    private void deleteImage(String imagePath) {
//        if (imagePath != null && !imagePath.equals("/img/member/member-default.png")) {
//            try {
//                Path filePath = Paths.get(uploadPath).resolve(imagePath);
//                Files.deleteIfExists(filePath);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}