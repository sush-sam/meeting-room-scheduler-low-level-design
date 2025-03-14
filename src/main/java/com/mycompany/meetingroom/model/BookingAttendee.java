package com.mycompany.meetingroom.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "booking_attendees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingAttendee {

    @EmbeddedId
    private BookingAttendeeId id = new BookingAttendeeId();

    @ManyToOne
    @MapsId("bookingId") // Maps to bookingId in BookingAttendeeId
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne
    @MapsId("userId") // Maps to userId in BookingAttendeeId
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
