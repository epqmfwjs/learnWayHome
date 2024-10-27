package com.learnway.member.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

// 컨설턴트 가입(등록)에 사용되는 DTO
@Getter
@Setter
public class ConsultantJoinDTO {

    @NotEmpty(message = "아이디는 필수 입력 항목입니다.")
    @Size(min = 4, max = 20, message = "아이디는 4자 이상, 20자 이하로 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "아이디는 영문과 숫자만 가능합니다.")
    private String username;

    @NotEmpty(message = "비밀번호는 필수 입력 항목입니다.")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()_+\\-=\\[\\]{};':\",./<>?])(?=.*[A-Za-z\\d~!@#$%^&*()_+\\-=\\[\\]{};':\",./<>?]).{6,}$",
            message = "비밀번호는 영문 대소문자, 숫자, 특수문자를 모두 포함해야 합니다.")
    private String password;

    @NotEmpty(message = "비밀번호 확인은 필수 입력 항목입니다.")
    private String confirmPassword;

    @NotEmpty(message = "이름은 필수 입력 항목입니다.")
    private String name;

    @NotEmpty(message = "담당 과목은 필수 입력 항목입니다.")
    private String subject;

    private String description;     // 설명

    private MultipartFile image;        // 프로필 이미지 URL

}
