package com.mycompany.meetingroom.service.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.meetingroom.model.Booking;
import com.mycompany.meetingroom.model.BookingAttendee;
import com.mycompany.meetingroom.model.BookingAttendeeId;
import com.mycompany.meetingroom.model.Room;
import com.mycompany.meetingroom.model.TimeSlot;
import com.mycompany.meetingroom.model.User;
import com.mycompany.meetingroom.repository.BookingRepository;
import com.mycompany.meetingroom.repository.BookingAttendeeRepository;
import com.mycompany.meetingroom.repository.RoomRepository;
import com.mycompany.meetingroom.repository.UserRepository;
import com.mycompany.meetingroom.request.BookingRequest;
import com.mycompany.meetingroom.request.BookingResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SingleBookingService implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final BookingAttendeeRepository bookingAttendeeRepository;

    @Override
    @Transactional
    public List<BookingResponse> createBooking(BookingRequest request) {
        // Validate room exists
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        // Validate organizer exists
        User organizer = userRepository.findById(request.getOrganizerId())
                .orElseThrow(() -> new IllegalArgumentException("Organizer not found"));

        // Create and save the booking
        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setOrganizer(organizer);
        TimeSlot timeSlot = new TimeSlot(request.getStartTime(), request.getEndTime());
        booking.setTimeSlot(timeSlot);

        Booking savedBooking = bookingRepository.save(booking);

        // Save attendees separately in BookingAttendee table
        saveAttendees(savedBooking, request.getAttendeeIds());

        return List.of(convertToResponse(savedBooking));
    }

    @Override
    public List<BookingResponse> getBookings(Long roomId, TimeSlot timeSlot) {
        List<Booking> bookings = bookingRepository.findBookingsByRoomAndTimeSlot(
                roomId, timeSlot.getStartTime(), timeSlot.getEndTime());

        return bookings.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BookingType getType() {
        return BookingType.SINGLE;
    }

    private void saveAttendees(Booking booking, List<Long> attendeeIds) {
        if (attendeeIds == null || attendeeIds.isEmpty()) {
            return;
        }

        List<User> attendees = userRepository.findAllById(attendeeIds);

        List<BookingAttendee> bookingAttendees = attendees.stream()
                .map(attendee -> new BookingAttendee(new BookingAttendeeId(booking.getId(), attendee.getId()), booking, attendee))
                .collect(Collectors.toList());

        bookingAttendeeRepository.saveAll(bookingAttendees);
    }

    private BookingResponse convertToResponse(Booking booking) {
        return BookingResponse.builder()
            .bookingId(booking.getId())
            .roomId(booking.getRoom().getId())
            .organizerId(booking.getOrganizer().getId())
            .startTime(booking.getTimeSlot().getStartTime())
            .endTime(booking.getTimeSlot().getEndTime())
            .build();
    }
}
