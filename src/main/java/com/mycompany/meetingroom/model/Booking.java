package com.mycompany.meetingroom.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizer;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Embedded
    private TimeSlot timeSlot;

    @Enumerated(EnumType.STRING)
    @Column(name = "recurrence_type")
    private RecurrenceType recurrenceType;

    @Column(name = "repeat_interval")
    private Integer repeatInterval; 

    @Column(name = "end_date")
    private LocalDate endDate;

    @PrePersist
    @PreUpdate
    private void validateBooking() {
        if (timeSlot != null) {
            timeSlot.validate();
        }
        if (repeatInterval != null && repeatInterval <= 0) {
            throw new IllegalArgumentException("Repeat interval must be greater than 0");
        }
    }
}
