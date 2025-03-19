package com.mycompany.meetingroom.controller;

import com.mycompany.meetingroom.model.Room;
import com.mycompany.meetingroom.request.RoomSearchRequest;
import com.mycompany.meetingroom.service.availability.AvailabilityService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final AvailabilityService availabilityService;

    /**
     * Endpoint to find all available rooms based on search criteria.
     *
     * @param searchRequest the search request containing time slot, capacity, floor, and building name
     * @return a list of available rooms matching the search criteria
     */
    @PostMapping("/rooms")
    public ResponseEntity<List<Room>> findAvailableRooms(@RequestBody RoomSearchRequest searchRequest) {
        List<Room> availableRooms = availabilityService.findAvailableRooms(searchRequest);
        return ResponseEntity.ok(availableRooms);
    }

}