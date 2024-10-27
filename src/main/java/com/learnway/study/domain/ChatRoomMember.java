package com.learnway.study.domain;

import java.util.List;
import java.util.Optional;

import com.learnway.member.domain.Member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="Chatroommember")
public class ChatRoomMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="chatmem_id", nullable = false)
    private Integer chatMemId;
    
    
    
    
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id", nullable = false)
    private Member member;
    
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "study_chatroomid", nullable = false)
    private ChatRoom chatRoom;
    
    @Column(name = "chatmem_has_entered", nullable = false)
    private boolean hasEntered;
}