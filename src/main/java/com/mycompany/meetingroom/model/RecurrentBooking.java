package com.mycompany.meetingroom.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recurrent_bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecurrentBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Embedded
    private TimeSlot timeSlot;
}
