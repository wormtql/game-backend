package com.example.game.game_spec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameSpecFactory {
    @Autowired
    private HexGameSpec hexGameSpec;

    public AbstractGameSpec getGameSpec(String type) {
        if (type.equals("hex")) {
            return hexGameSpec;
        }

        return null;
    }
}
