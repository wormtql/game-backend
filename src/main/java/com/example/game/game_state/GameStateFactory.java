package com.example.game.game_state;

import org.springframework.stereotype.Component;

@Component
public class GameStateFactory {
    public AbstractGameState getNewGameState(String type) {
        if (type.equals("hex")) {
            return new HexGameState();
        }

        return null;
    }
}
