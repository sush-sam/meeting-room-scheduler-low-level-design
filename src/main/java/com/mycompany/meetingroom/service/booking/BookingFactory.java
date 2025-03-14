package com.mycompany.meetingroom.service.booking;

import com.mycompany.meetingroom.request.BookingRequest;
import com.mycompany.meetingroom.request.BookingResponse;
import com.mycompany.meetingroom.model.TimeSlot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingFactory {

    private final List<BookingService> bookingServices;  // Injected services

    public List<BookingResponse> createBooking(BookingRequest request) {
        BookingService service = getBookingService(request);
        return service.createBooking(request);
    }

    public List<BookingResponse> getBookings(TimeSlot timeSlot) {
        return bookingServices.stream()
                .flatMap(service -> service.getBookings(timeSlot).stream())
                .collect(Collectors.toList());
    }

    private BookingService getBookingService(BookingRequest request) {
        BookingType type = (request.getRepeatInterval() != null) ? BookingType.RECURRENT : BookingType.SINGLE;
        return bookingServices.stream()
                .filter(service -> service.getType() == type)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No service found for type: " + type));
    }
}

