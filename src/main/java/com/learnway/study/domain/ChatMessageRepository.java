package com.learnway.study.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

	
	List<ChatMessage> findByChatroom_Chatroomid(int roomId);
}
