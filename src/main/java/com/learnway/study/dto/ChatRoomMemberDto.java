package com.learnway.study.dto;

import com.learnway.member.domain.Member;
import com.learnway.study.domain.ChatRoom;

import lombok.Data;

@Data
public class ChatRoomMemberDto {

	
	 private Integer chatMemId;
	 private Member member;
	 private ChatRoom chatRoom;
	 private boolean hasEntered;
}
