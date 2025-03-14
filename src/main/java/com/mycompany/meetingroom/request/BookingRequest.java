package com.mycompany.meetingroom.request;

import java.util.List;

import com.mycompany.meetingroom.model.RecurrenceType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
    private Long bookingId;
    private Long organizerId;
    private Long roomId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private RecurrenceType recurrenceType; // Null for single bookings, values like "daily", "weekly", etc.
    private Integer repeatInterval; // Null if not recurrent
    private LocalDate endDate; // Null if not recurrent
    private List<Long> attendeeIds; // List of user IDs for attendees
}
