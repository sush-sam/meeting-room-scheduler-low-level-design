package com.mycompany.meetingroom.controller;

import com.mycompany.meetingroom.request.BookingRequest;
import com.mycompany.meetingroom.request.BookingResponse;
import com.mycompany.meetingroom.model.TimeSlot;
import com.mycompany.meetingroom.service.booking.BookingFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingFactory bookingFactory;

    @PostMapping
    public ResponseEntity<List<BookingResponse>> createBooking(@RequestBody BookingRequest request) {
        List<BookingResponse> responses = bookingFactory.createBooking(request);
        return ResponseEntity.ok(responses);
    }

    @GetMapping
    public ResponseEntity<List<BookingResponse>> getBookings(
            @RequestParam Long roomId,
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime) {
        TimeSlot timeSlot = new TimeSlot(startTime, endTime);
        List<BookingResponse> responses = bookingFactory.getBookings(roomId, timeSlot);
        return ResponseEntity.ok(responses);
    }
}