package com.mycompany.meetingroom.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomRequest {
    private String name;
    private String building;
    private Integer floor;
    private Integer capacity;
}
