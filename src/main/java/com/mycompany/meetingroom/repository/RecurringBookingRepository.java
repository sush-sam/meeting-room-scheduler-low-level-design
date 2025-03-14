package com.mycompany.meetingroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mycompany.meetingroom.model.RecurrentBooking;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecurringBookingRepository extends JpaRepository<RecurrentBooking, Long> {

    @Query("SELECT rb FROM RecurringBooking rb " +
           "WHERE rb.startTime <= :endTime " +
           "AND rb.endTime >= :startTime")
    List<RecurrentBooking> findRecurringBookingsWithinTimeSlot(LocalDateTime startTime, LocalDateTime endTime);
}

