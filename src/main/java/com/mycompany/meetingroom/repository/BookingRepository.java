package com.mycompany.meetingroom.repository;

import com.mycompany.meetingroom.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.startTime >= :startTime AND b.endTime <= :endTime")
    List<Booking> findBookingsWithinTimeSlot(LocalDateTime startTime, LocalDateTime endTime);
}
