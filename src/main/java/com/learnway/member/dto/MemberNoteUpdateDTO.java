package com.learnway.member.dto;

import lombok.Data;

@Data
// 어드민 페이지에서 멤버 노트(비고)를 업데이트 시 사용되는 DTO
public class MemberNoteUpdateDTO {
    private Long id;
    private String note;
}

