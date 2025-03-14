package com.mycompany.meetingroom.service.room;

import com.mycompany.meetingroom.request.RoomRequest;
import com.mycompany.meetingroom.request.RoomResponse;
import com.mycompany.meetingroom.model.Room;
import com.mycompany.meetingroom.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public RoomResponse addRoom(RoomRequest request) {
        Room room = Room.builder()
                .name(request.getName())
                .building(request.getBuilding())
                .floor(request.getFloor())
                .capacity(request.getCapacity())
                .build();

        Room savedRoom = roomRepository.save(room);

        return new RoomResponse(
                savedRoom.getId(),
                savedRoom.getName(),
                savedRoom.getBuilding(),
                savedRoom.getFloor(),
                savedRoom.getCapacity()
        );
    }

    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(room -> new RoomResponse(
                        room.getId(),
                        room.getName(),
                        room.getBuilding(),
                        room.getFloor(),
                        room.getCapacity()
                ))
                .collect(Collectors.toList());
    }

    public Optional<RoomResponse> getRoomById(Long id) {
        return roomRepository.findById(id)
                .map(room -> new RoomResponse(
                        room.getId(),
                        room.getName(),
                        room.getBuilding(),
                        room.getFloor(),
                        room.getCapacity()
                ));
    }
}
