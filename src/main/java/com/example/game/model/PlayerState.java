package com.example.game.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PlayerState {
    private boolean ready;
    private String username;
    private int role;
}
