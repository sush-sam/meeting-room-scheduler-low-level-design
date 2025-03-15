package com.mycompany.meetingroom.repository;

import com.mycompany.meetingroom.model.RecurrentBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RecurringBookingRepository extends JpaRepository<RecurrentBooking, Long> {

    @Query("SELECT rb FROM RecurrentBooking rb " +
           "WHERE rb.booking.room.id = :roomId " +
           "AND rb.timeSlot.startTime >= :startTime " +
           "AND rb.timeSlot.endTime <= :endTime")
    List<RecurrentBooking> findBookingsByRoomAndTimeSlot(@Param("roomId") Long roomId,
                                                          @Param("startTime") LocalDateTime startTime,
                                                          @Param("endTime") LocalDateTime endTime);
}
