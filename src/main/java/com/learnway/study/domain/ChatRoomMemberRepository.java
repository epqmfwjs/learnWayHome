package com.learnway.study.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
	
	List<ChatRoomMember> findByMember_MemberId(String memberId);
	 List<ChatRoomMember> findByChatRoom_Chatroomid(int roomId);
	
}
