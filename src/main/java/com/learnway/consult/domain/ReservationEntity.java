package com.learnway.consult.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.learnway.member.domain.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    
    @ManyToOne
    @JoinColumn(name = "counselor_id")
    private Consultant counselor;
    
    private String reservationContent;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime bookingStart;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime bookingEnd;
	
	public ReservationEntity() {
	}

    public ReservationEntity(Long id, Member member, Consultant counselor, String reservationContent,LocalDateTime bookingStart,
            LocalDateTime bookingEnd) {
		this.id = id;
		this.member = member;
		this.counselor = counselor;
		this.reservationContent = reservationContent;
		this.bookingStart = bookingStart;
		this.bookingEnd = bookingEnd;
		}
    
}
