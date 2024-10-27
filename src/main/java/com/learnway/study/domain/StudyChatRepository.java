package com.learnway.study.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface StudyChatRepository extends JpaRepository<ChatRoom, Integer> {

	List<ChatRoom> findByStudyPostid(int postid);
	
	 @Query("SELECT c.roomname FROM ChatRoom c WHERE c.chatroomid = :roomId")
	    String findRoomNameByRoomId(@Param("roomId") Integer roomId);
	
}
