package com.learnway.study.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {

    @Query("SELECT cr.study.postid FROM ChatRoom cr")
    List<Integer> findAllPostIds();
    
    @Query("SELECT s.postid FROM Study s LEFT JOIN s.chatroom c WHERE c.chatroomid IS NULL")
    List<Integer> findStudyPostIdsWithoutChatRooms();

    List<ChatRoom> findByMember_MemberId(String memberId);
}
