package com.mycompany.meetingroom.service.availability;


import com.mycompany.meetingroom.model.Room;
import com.mycompany.meetingroom.model.TimeSlot;
import com.mycompany.meetingroom.request.BookingResponse;
import com.mycompany.meetingroom.request.RoomSearchRequest;
import com.mycompany.meetingroom.repository.RoomRepository;
import com.mycompany.meetingroom.service.booking.BookingServiceRegistry;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvailabilityService {

    private final BookingServiceRegistry bookingServiceRegistry;
    private final RoomRepository roomRepository;

    public boolean isRoomAvailable(Long roomId, TimeSlot timeSlot) {
        return getBookings(roomId, timeSlot).isEmpty();
    }

        /**
     * Finds all available rooms for the given search criteria.
     *
     * @param searchRequest the search request containing time slot, capacity, floor, and building name
     * @return a list of available rooms matching the search criteria
     */
    public List<Room> findAvailableRooms(RoomSearchRequest searchRequest) {
        // Get all rooms
        List<Room> allRooms = roomRepository.findAll();

        // Filter rooms based on availability and search criteria
        return allRooms.stream()
                .filter(room -> isRoomAvailable(room.getId(), searchRequest.getTimeSlot()))
                .filter(room -> searchRequest.getCapacity() == null || room.getCapacity() >= searchRequest.getCapacity())
                .filter(room -> searchRequest.getFloor() == null || room.getFloor().equals(searchRequest.getFloor()))
                .filter(room -> searchRequest.getBuildingName() == null || room.getBuilding().equalsIgnoreCase(searchRequest.getBuildingName()))
                .collect(Collectors.toList());
    }


    public List<BookingResponse> getBookings(Long roomId, TimeSlot timeSlot) {
        return bookingServiceRegistry.getBookingServices().stream()  //
                .flatMap(service -> service.getBookings(roomId, timeSlot).stream())
                .collect(Collectors.toList());
    }
}
