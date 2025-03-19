package com.mycompany.meetingroom.service.booking;

import com.mycompany.meetingroom.request.BookingRequest;
import com.mycompany.meetingroom.request.BookingResponse;
import com.mycompany.meetingroom.service.availability.AvailabilityService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingFactory {

    private final BookingServiceRegistry bookingServiceRegistry;
    private final AvailabilityService availabilityService;

    public List<BookingResponse> createBooking(BookingRequest request) {
        IBookingService service = bookingServiceRegistry.getBookingService(request);
        return service.createBooking(request, availabilityService::isRoomAvailable);
    }
}