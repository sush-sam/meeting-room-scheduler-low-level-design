package com.mycompany.meetingroom.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomResponse {
    private Long id;
    private String name;
    private String building;
    private Integer floor;
    private Integer capacity;
}
