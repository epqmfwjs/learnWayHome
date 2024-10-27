package com.learnway.consult.dto;

import lombok.Data;

@Data
public class ReservationDTO {
    private Long id;
    private String clientName;
    private String bookingStart;
    private String bookingEnd;

    
    public ReservationDTO() {
    }

    public ReservationDTO(Long user_id, String clientName, String bookingStart, String bookingEnd) {
        this.id = user_id;
        this.clientName = clientName;
        this.bookingStart = bookingStart;
        this.bookingEnd = bookingEnd;
    }


}

