package com.learnway.global.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

// 기준 정보(Material/Subject/StudyWay) 통합 DTO
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MasterDataDTO {

    @NotBlank(message="카테고리를 선택해 주세요!")                  // 필수 입력 (선택)
    private String category;                                    // 정보 선택 (Material/Subject/StudyWay)

    @NotBlank(message="Code를 입력해 주세요!")                     // 필수 입력
    @Size(min=4, max=4, message = "Code는 4자리로 입력해주세요!")   // 4자리 제한
    private String code;                                         // 각 코드 4자리 (PK)

    @NotBlank(message="이름을 입력해 주세요!")                     // 필수 입력
    private String name;                                        // 기준 정보 이름

    private String note;                                        // 기준 정보 비고(노트) - 생략 가능
}
