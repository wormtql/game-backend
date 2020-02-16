package com.example.game.game_state;

import com.alibaba.fastjson.JSONObject;

public interface AbstractGameState {
    void updateGameState(JSONObject move);

    void refresh();

    int winner();
}
