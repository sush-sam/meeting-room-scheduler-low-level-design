package com.mycompany.meetingroom.service.booking;

import com.mycompany.meetingroom.model.TimeSlot;
import com.mycompany.meetingroom.request.BookingRequest;
import com.mycompany.meetingroom.request.BookingResponse;
import java.util.List;

public interface BookingService {
    List<BookingResponse> createBooking(BookingRequest request);
    List<BookingResponse> getBookings(TimeSlot timeSlot);
    BookingType getType();
}