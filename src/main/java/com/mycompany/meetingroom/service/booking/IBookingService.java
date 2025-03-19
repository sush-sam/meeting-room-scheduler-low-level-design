package com.mycompany.meetingroom.service.booking;

import com.mycompany.meetingroom.model.TimeSlot;
import com.mycompany.meetingroom.request.BookingRequest;
import com.mycompany.meetingroom.request.BookingResponse;
import java.util.List;
import java.util.function.BiPredicate;


public interface IBookingService {
        List<BookingResponse> createBooking(BookingRequest request, BiPredicate<Long, TimeSlot> isRoomAvailable);
        public List<BookingResponse> getBookings(Long roomId, TimeSlot timeSlot);
        BookingType getType();
}