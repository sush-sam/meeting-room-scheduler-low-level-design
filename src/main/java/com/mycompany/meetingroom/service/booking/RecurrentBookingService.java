package com.mycompany.meetingroom.service.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.meetingroom.model.*;
import com.mycompany.meetingroom.repository.*;
import com.mycompany.meetingroom.request.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecurrentBookingService implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final BookingAttendeeRepository bookingAttendeeRepository;
    private final RecurringBookingRepository recurrentBookingRepository;

    @Override
    @Transactional
    public List<BookingResponse> createBooking(BookingRequest request) {
        if (request.getRepeatInterval() <= 0) {
            throw new IllegalArgumentException("Invalid request type for recurrent booking");
        }

        // Validate room exists
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        // Validate organizer exists
        User organizer = userRepository.findById(request.getOrganizerId())
                .orElseThrow(() -> new IllegalArgumentException("Organizer not found"));

        // Create parent booking entry
        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setOrganizer(organizer);
        booking.setTimeSlot(new TimeSlot(request.getStartTime(), request.getEndTime()));
        booking.setRecurrenceType(request.getRecurrenceType());
        booking.setRepeatInterval(request.getRepeatInterval());
        booking.setEndDate(request.getEndDate());
        
        Booking savedBooking = bookingRepository.save(booking);

        // Generate occurrences
        List<RecurrentBooking> occurrences = generateOccurrences(savedBooking, request);
        recurrentBookingRepository.saveAll(occurrences);

        // Save attendees
        saveAttendees(savedBooking, request.getAttendeeIds());

        return occurrences.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public List<BookingResponse> getBookings(Long roomId, TimeSlot timeSlot) {
        List<RecurrentBooking> occurrences = recurrentBookingRepository.findBookingsByRoomAndTimeSlot(
                roomId, timeSlot.getStartTime(), timeSlot.getEndTime()
        );

        return occurrences.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BookingType getType() {
        return BookingType.RECURRENT;
    }

    private List<RecurrentBooking> generateOccurrences(Booking booking, BookingRequest request) {
        List<RecurrentBooking> occurrences = new ArrayList<>();
        LocalDateTime occurrenceTime = request.getStartTime();
        LocalDateTime endDate = request.getEndDate().atTime(request.getEndTime().toLocalTime());

        while (occurrenceTime.isBefore(endDate)) {
            TimeSlot timeSlot = new TimeSlot(occurrenceTime, occurrenceTime.plusMinutes(
                    request.getEndTime().getMinute() - request.getStartTime().getMinute()
            ));

            RecurrentBooking recurrentBooking = new RecurrentBooking();
            recurrentBooking.setBooking(booking);
            recurrentBooking.setTimeSlot(timeSlot);
            occurrences.add(recurrentBooking);
            occurrenceTime = getNextOccurrence(occurrenceTime, request);
        }
        return occurrences;
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

    private BookingResponse convertToResponse(RecurrentBooking recurrentBooking) {
        return BookingResponse.builder()
                .bookingId(recurrentBooking.getBooking().getId())
                .roomId(recurrentBooking.getBooking().getRoom().getId())
                .organizerId(recurrentBooking.getBooking().getOrganizer().getId())
                .startTime(recurrentBooking.getTimeSlot().getStartTime())
                .endTime(recurrentBooking.getTimeSlot().getEndTime())
                .recurrenceId(recurrentBooking.getId())
                .build();
    }
}
