package com.learnway.member.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// 회원 가입 시 사용되는 DTO
@Getter
@Setter
@ToString
public class JoinDTO {

    @NotEmpty(message = "아이디는 필수 입력 항목입니다.")
    @Size(min = 4, max = 20, message = "아이디는 4자 이상, 20자 이하로 입력해주세요.")
    private String username;        // 유저 ID : memberId

    @NotEmpty(message = "비밀번호는 필수 입력 항목입니다.")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()_+\\-=\\[\\]{};':\",./<>?])(?=.*[A-Za-z\\d~!@#$%^&*()_+\\-=\\[\\]{};':\",./<>?]).{6,}$",
            message = "비밀번호는 영문 대소문자, 숫자, 특수문자를 모두 포함해야 합니다.")
    private String password;        // 유저 PW : memberPW

    @NotEmpty(message = "비밀번호 확인은 필수 입력 항목입니다.")
    private String confirmPassword; // 유저 비밀번호 확인 : memberPW

    @NotEmpty(message = "이름은 필수 입력 항목입니다.")
    private String name;

    @NotEmpty(message = "생년월일은 필수 입력 항목입니다.")
    private String birth;           // 생년월일 (yyyy-MM-dd 형식)

    @NotEmpty(message = "연락처는 필수 입력 항목입니다.")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "연락처 형식을 확인해 주세요.")
    private String phone;           // 연락처

    @NotEmpty(message = "통신사는 필수 입력 항목입니다.")
    private String telecom;         // 통신사

    @NotEmpty(message = "메일 인증을 진행해 주세요.")
    private String email;           // 이메일

    @NotEmpty(message = "성별은 필수로 선택해 주세요.")
    private String gender; // 성별

    // 하기는 선택 사항
    private String school;          // 학교

    private int grade;              // 학년

    private String address;         // 주소 (카카오맵 선택)

    private String detailAddress;   // 상세 주소 (나머지 주소)

    private MultipartFile image;    // 프로필 이미지

    private List<TargetUniDTO> targetUni; // JOIN / 목표 대학
}