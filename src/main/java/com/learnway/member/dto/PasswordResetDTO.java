package com.learnway.member.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 비밀번호 찾기 시 리셋 진행 DTO
public class PasswordResetDTO {

    @NotEmpty(message = "비밀번호는 필수 입력 항목입니다.")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()_+\\-=\\[\\]{};':\",./<>?])(?=.*[A-Za-z\\d~!@#$%^&*()_+\\-=\\[\\]{};':\",./<>?]).{6,}$",
            message = "비밀번호는 영문 대소문자, 숫자, 특수문자를 모두 포함해야 합니다.")
    private String password;

    @NotEmpty(message = "비밀번호 확인은 필수 입력 항목입니다.")
    private String confirmPassword;
}