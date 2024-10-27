package com.learnway.consult.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


	@Repository
	public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
		//예약하기 들어왔을때 달력에 예약일정 표시하기위한 jpa
		List<ReservationEntity> findBycounselor_id(Long consultantId);
		
		//상담사가 자기 예약 리스트 확인할때 jpa
		List<ReservationEntity> getReservationsListByCounselor_id(Long counselor_id);
		
		//멤버가 자기 상담리스트 조회
		List<ReservationEntity> findBymember_id(Long memberId);
	}
