package com.learnway.study.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.learnway.member.domain.Member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="ChatMessage")
public class ChatMessage {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="chat_msg_id", nullable = false)
    private Integer msgid;
    
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "study_chatroomid", nullable = false)
    @JsonBackReference
    private ChatRoom chatroom;
 
    
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id", nullable = false)
    private Member member;

    @Column(name="chat_msg", nullable = false)
    private String msg;
    
    @Column(name="chat_date", nullable = false)
    private LocalDateTime datetime;
    
    @Column(name="unread_count", nullable = true)
    private Integer unread;
}