package com.example.game.service;

import com.alibaba.fastjson.JSONObject;
import com.example.game.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService {
    private Map<String, Integer> userIdBySessionId = new ConcurrentHashMap<>();
    private Map<Integer, Session> sessionByUserId = new ConcurrentHashMap<>();
    private Map<String, Integer> roomIdBySessionId = new ConcurrentHashMap<>();

    public void addSession(Session session, int userId, int roomId) {
        userIdBySessionId.put(session.getId(), userId);
        sessionByUserId.put(userId, session);

        roomIdBySessionId.put(session.getId(), roomId);
    }

    public boolean isSessionExist(Session session) {
        return userIdBySessionId.containsKey(session.getId());
    }

    public void send(Session session, String text) throws IOException {
        session.getBasicRemote().sendText(text);
    }

    public int getRoomIdBySessionId(String sessionId) {
        return roomIdBySessionId.get(sessionId);
    }

    public int getUserIdBySessionId(String sessionId) {
        return userIdBySessionId.get(sessionId);
    }

    public void removeSession(Session session) {
        String sessionId = session.getId();
        int userId = userIdBySessionId.remove(sessionId);
        sessionByUserId.remove(userId);
        roomIdBySessionId.remove(sessionId);
    }

    public void sendToRoomExceptUser(Room room, int userId, JSONObject json) {
        try {
            for (int u : room.getPlayers().keySet()) {
                if (u != userId) {
                    Session session = sessionByUserId.get(u);
                    this.send(session, json.toJSONString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendToRoom(Room room, JSONObject json) {
        try {
            for (int u : room.getPlayers().keySet()) {
                Session session = sessionByUserId.get(u);
                this.send(session, json.toJSONString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToUser(int userId, JSONObject json) {
        Session session = sessionByUserId.get(userId);
        try {
            session.getBasicRemote().sendText(json.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendToSession(Session session, JSONObject json) {
        try {
            session.getBasicRemote().sendText(json.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
