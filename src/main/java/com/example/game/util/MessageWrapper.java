package com.example.game.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.game.model.Room;
import org.springframework.stereotype.Service;

import java.util.Map;

public class MessageWrapper {
    public static JSONObject gameOverMessage(boolean win) {
        JSONObject json = new JSONObject();
        json.put("command", "over");
        if (win) {
            json.put("value", "win");
        } else {
            json.put("value", "lose");
        }
        return json;
    }

    public static JSONObject playerQuitMessage(int userId) {
        JSONObject json = new JSONObject();
        json.put("command", "player_quit");
        json.put("value", userId);
        return json;
    }

    public static JSONObject roomTokenMessage(String token) {
        JSONObject json = new JSONObject();
        json.put("command", "room_token");
        json.put("token", token);
        return json;
    }

    public static JSONObject errorMessage(String message) {
        JSONObject json = new JSONObject();
        json.put("command", "error");
        json.put("message", message);
        return json;
    }

    public static JSONObject updateMemberMessage(Room room) {
        JSONObject json = new JSONObject();
        JSONArray arr = new JSONArray();

        for (int u : room.getPlayerName().keySet()) {
            JSONObject temp = new JSONObject();
            temp.put("name", room.getPlayerName().get(u));
            temp.put("status", room.getPlayers().get(u));

            arr.add(temp);
        }

        json.put("command", "update");
        json.put("data", arr);

        return json;
    }

    public static JSONObject startGameMessage() {
        JSONObject json = new JSONObject();
        json.put("command", "start");

        return json;
    }
}
