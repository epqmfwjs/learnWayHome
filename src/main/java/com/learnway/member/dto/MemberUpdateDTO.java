package com.learnway.member.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdateDTO {
    @NotEmpty(message = "아이디는 필수 입력 항목입니다.")
    private String memberId;

    @NotEmpty(message = "이름은 필수 입력 항목입니다.")
    private String memberName;

    private String password;
    private String confirmPassword;

    @NotEmpty(message = "생년월일은 필수 입력 항목입니다.")
    private String memberBirth;

    @NotEmpty(message = "연락처는 필수 입력 항목입니다.")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "연락처 형식을 확인해 주세요.")
    private String memberPhone;

    @NotEmpty(message = "통신사는 필수 입력 항목입니다.")
    private String memberTelecom;

    @NotEmpty(message = "이메일은 필수 입력 항목입니다.")
    private String memberEmail;

    @NotEmpty(message = "성별은 필수로 선택해 주세요.")
    private String memberGender; // 성별

    private String memberSchool;
    private int memberGrade;
    private String memberAddress;
    private String memberDetailadd;
    private String memberImage; // 이미지 URL
    private MultipartFile newMemberImage; // 새로운 이미지 파일

    private List<TargetUniDTO> targetUnis;
}