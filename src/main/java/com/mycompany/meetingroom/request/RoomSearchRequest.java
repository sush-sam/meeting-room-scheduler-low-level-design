package com.mycompany.meetingroom.request;

import com.mycompany.meetingroom.model.TimeSlot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomSearchRequest {
    private TimeSlot timeSlot;
    private Integer capacity;
    private Integer floor;
    private String buildingName;
}