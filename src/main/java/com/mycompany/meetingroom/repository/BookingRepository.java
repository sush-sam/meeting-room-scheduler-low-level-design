package com.mycompany.meetingroom.repository;

import com.mycompany.meetingroom.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.room.id = :roomId " +
           "AND b.timeSlot.startTime < :endTime AND b.timeSlot.endTime > :startTime")
    List<Booking> findBookingsByRoomAndTimeSlot(@Param("roomId") Long roomId, 
                                                @Param("startTime") LocalDateTime startTime, 
                                                @Param("endTime") LocalDateTime endTime);
}

