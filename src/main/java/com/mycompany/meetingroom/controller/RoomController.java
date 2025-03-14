package com.mycompany.meetingroom.controller;

import com.mycompany.meetingroom.request.RoomRequest;
import com.mycompany.meetingroom.request.RoomResponse;
import com.mycompany.meetingroom.service.room.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public ResponseEntity<RoomResponse> addRoom(@RequestBody RoomRequest request) {
        RoomResponse newRoom = roomService.addRoom(request);
        return ResponseEntity.ok(newRoom);
    }

    @GetMapping
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long id) {
        return roomService.getRoomById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
