package com.mycompany.meetingroom.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private Long recurrenceId; // Null for single bookings
    private Long bookingId;
    private Long organizerId;
    private Long roomId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}

