package com.learnway.member.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ConsultantUpdateDTO {

    @NotEmpty(message = "아이디는 필수 입력 항목입니다.")
    private String username;

    private String password;

    private String confirmPassword;

    @NotEmpty(message = "이름은 필수 입력 항목입니다.")
    private String name;

    @NotEmpty(message = "담당 과목은 필수 입력 항목입니다.")
    private String subject;

    private String description; // 설명

    private MultipartFile image; // 프로필 이미지

    private String imageUrl; // 프로필 이미지 URL
}
