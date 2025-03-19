package com.mycompany.meetingroom.service.booking;

import com.mycompany.meetingroom.exception.RoomNotAvailableException;
import com.mycompany.meetingroom.model.*;
import com.mycompany.meetingroom.repository.*;
import com.mycompany.meetingroom.request.BookingRequest;
import com.mycompany.meetingroom.request.BookingResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecurrentBookingService implements IBookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final BookingAttendeeRepository bookingAttendeeRepository;
    private final RecurringBookingRepository recurrentBookingRepository;

    @Override
    @Transactional
    public List<BookingResponse> createBooking(BookingRequest request, BiPredicate<Long, TimeSlot> isRoomAvailable) {
        if (request.getRepeatInterval() <= 0) {
            throw new IllegalArgumentException("Invalid request type for recurrent booking");
        }

        // Validate room exists
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        // Validate organizer exists
        User organizer = userRepository.findById(request.getOrganizerId())
                .orElseThrow(() -> new IllegalArgumentException("Organizer not found"));

        // Check room availability for the initial time slot
        TimeSlot initialTimeSlot = new TimeSlot(request.getStartTime(), request.getEndTime());
        if (!isRoomAvailable.test(room.getId(), initialTimeSlot)) {
            throw new RoomNotAvailableException("Room is not available for the initial time slot.");
        }

        // Create parent booking entry
        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setOrganizer(organizer);
        booking.setTimeSlot(initialTimeSlot);
        booking.setRecurrenceType(request.getRecurrenceType());
        booking.setRepeatInterval(request.getRepeatInterval());
        booking.setEndDate(request.getEndDate());

        Booking savedBooking = bookingRepository.save(booking);

        // Save attendees
        saveAttendees(savedBooking, request.getAttendeeIds());

        // Generate occurrences and collect responses
        return generateOccurrences(savedBooking, request, isRoomAvailable);
    }

    @Override
    public List<BookingResponse> getBookings(Long roomId, TimeSlot timeSlot) {
        List<RecurrentBooking> occurrences = recurrentBookingRepository.findBookingsByRoomAndTimeSlot(
                roomId, timeSlot.getStartTime(), timeSlot.getEndTime()
        );

        return occurrences.stream()
                .map(recurrentBooking -> BookingResponse.builder()
                        .bookingId(recurrentBooking.getBooking().getId())
                        .roomId(recurrentBooking.getBooking().getRoom().getId())
                        .organizerId(recurrentBooking.getBooking().getOrganizer().getId())
                        .startTime(recurrentBooking.getTimeSlot().getStartTime())
                        .endTime(recurrentBooking.getTimeSlot().getEndTime())
                        .recurrenceId(recurrentBooking.getId())
                        .status("SUCCESS")
                        .build())
                .toList();
    }

    @Override
    public BookingType getType() {
        return BookingType.RECURRENT;
    }

    private List<BookingResponse> generateOccurrences(Booking booking, BookingRequest request, BiPredicate<Long, TimeSlot> isRoomAvailable) {
        List<BookingResponse> bookingResponses = new ArrayList<>();
        LocalDateTime occurrenceTime = request.getStartTime();
        LocalDateTime endDate = request.getEndDate().atTime(request.getEndTime().toLocalTime());

        while (occurrenceTime.isBefore(endDate)) {
            TimeSlot timeSlot = new TimeSlot(occurrenceTime, occurrenceTime.plusMinutes(
                    request.getEndTime().getMinute() - request.getStartTime().getMinute()
            ));

            BookingResponse response;
            if (isRoomAvailable.test(booking.getRoom().getId(), timeSlot)) {
                // Room is available, create the occurrence
                RecurrentBooking recurrentBooking = new RecurrentBooking();
                recurrentBooking.setBooking(booking);
                recurrentBooking.setTimeSlot(timeSlot);
                recurrentBookingRepository.save(recurrentBooking);

                // Add SUCCESS response
                response = BookingResponse.builder()
                        .bookingId(booking.getId())
                        .roomId(booking.getRoom().getId())
                        .organizerId(booking.getOrganizer().getId())
                        .startTime(timeSlot.getStartTime())
                        .endTime(timeSlot.getEndTime())
                        .recurrenceId(recurrentBooking.getId())
                        .status("SUCCESS")
                        .build();
            } else {
                // Room is not available, add FAILURE response
                response = BookingResponse.builder()
                        .bookingId(booking.getId())
                        .roomId(booking.getRoom().getId())
                        .organizerId(booking.getOrganizer().getId())
                        .startTime(timeSlot.getStartTime())
                        .endTime(timeSlot.getEndTime())
                        .status("FAILURE")
                        .build();
            }

            bookingResponses.add(response);
            occurrenceTime = getNextOccurrence(occurrenceTime, request);
        }
        return bookingResponses;
    }

    private LocalDateTime getNextOccurrence(LocalDateTime current, BookingRequest request) {
        switch (request.getRecurrenceType()) {
            case DAILY:
                return current.plusDays(request.getRepeatInterval());
            case WEEKLY:
                return current.plusWeeks(request.getRepeatInterval());
            case BIWEEKLY:
                return current.plusWeeks(request.getRepeatInterval() * 2);
            case MONTHLY:
                return current.plusMonths(request.getRepeatInterval());
            case YEARLY:
                return current.plusYears(request.getRepeatInterval());
            default:
                throw new IllegalArgumentException("Unsupported recurrence type");
        }
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
}
