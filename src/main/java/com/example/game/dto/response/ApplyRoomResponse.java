package com.example.game.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApplyRoomResponse {
    private int id;
    private int status;
    private String message;
}
