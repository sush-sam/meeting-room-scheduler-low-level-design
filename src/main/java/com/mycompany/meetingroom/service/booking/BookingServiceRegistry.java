package com.mycompany.meetingroom.service.booking;

import com.mycompany.meetingroom.request.BookingRequest;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingServiceRegistry {

    private final List<IBookingService> bookingServices; // ✅ Injected services✅ Injected services

    // ✅ Get the right service based on BookingRequest
    public IBookingService getBookingService(BookingRequest request) {
        BookingType type = (request.getRepeatInterval() != null) ? BookingType.RECURRENT : BookingType.SINGLE;
        return bookingServices.stream()
                .filter(service -> service.getType() == type)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No service found for type: " + type));
    }

    public List<IBookingService> getBookingServices() {
        return bookingServices;
    }
}

