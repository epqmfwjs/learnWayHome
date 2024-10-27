package com.learnway.consult.dto;

import lombok.Data;

@Data
public class UserInfoDTO {

    private Long id;//pk값
    private String memberName;//이름
    private String memberAge;//나이
    private String memberGender; // 성별
    private String memberPhone;//연락처
    private String memberEmail;//이메일
    private String memberAllAddress;// 주소 상제주소 합친것
    private String memberSchool;//학교
    private int memberGrade;//학년
    private String memberImg;//프로필이미지
    private String requestContents;//간단한 요청상담내용
    
    
    public UserInfoDTO() {
    }

	public UserInfoDTO(Long id, String memberName , String memberAge, String memberGender, 
			String memberPhone, String memberEmail, String memberAllAddress, String memberSchool,
			int memberGrade, String memberImg,String requestContents) {
		this.id = id;
		this.memberName = memberName;
		this.memberAge = memberAge;
		this.memberGender = memberGender;
		this.memberPhone = memberPhone;
		this.memberEmail = memberEmail;
		this.memberAllAddress = memberAllAddress;
		this.memberSchool = memberSchool;
		this.memberGrade = memberGrade;
		this.memberImg = memberImg;
		this.requestContents = requestContents;

	}




}

