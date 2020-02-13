package com.example.game.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinRoomRequestObject {
    private int roomId;
    private String password;
}
