package com.mycompany.meetingroom.repository;

import com.mycompany.meetingroom.model.BookingAttendee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingAttendeeRepository extends JpaRepository<BookingAttendee, com.mycompany.meetingroom.model.BookingAttendeeId> {
    List<BookingAttendee> findByBookingId(Long bookingId);
}