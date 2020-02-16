package com.example.game.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.game.model.Room;
import org.springframework.stereotype.Service;

import java.util.Map;

public class MessageWrapper {
    public static JSONObject gameOverMessage(int winnerRole) {
        JSONObject json = new JSONObject();
        json.put("command", "over");
        json.put("role", winnerRole);
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

    public static JSONObject roomIdMessage(int id) {
        JSONObject json = new JSONObject();
        json.put("command", "room_id");
        json.put("id", id);
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

        for (int u : room.getPlayers().keySet()) {
            JSONObject temp = new JSONObject();
            temp.put("name", room.getPlayers().get(u).getUsername());
            temp.put("status", room.getPlayers().get(u).isReady());

            arr.add(temp);
        }

        json.put("command", "update");
        json.put("data", arr);

        return json;
    }

    public static JSONObject startGameMessage(int role) {
        JSONObject json = new JSONObject();
        json.put("command", "start");
        json.put("role", role);

        return json;
    }

    public static JSONObject yourTurnMessage() {
        JSONObject json = new JSONObject();
        json.put("command", "your_turn");
        return json;
    }

    public static JSONObject playMessage(JSONObject play) {
        JSONObject json = new JSONObject();
        json.put("command", "play");
        json.put("play", play);
        return json;
    }
}
