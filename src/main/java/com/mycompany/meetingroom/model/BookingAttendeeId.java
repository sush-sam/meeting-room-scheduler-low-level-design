package com.mycompany.meetingroom.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingAttendeeId implements Serializable {
    private Long bookingId;
    private Long userId;
}

