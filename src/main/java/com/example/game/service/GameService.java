package com.example.game.service;

import com.alibaba.fastjson.JSONObject;
import com.example.game.model.Room;
import com.example.game.util.MessageWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private RoomStateService roomStateService;
    private SessionService sessionService;

    @Autowired
    public GameService(RoomStateService roomStateService, SessionService sessionService) {
        this.roomStateService = roomStateService;
        this.sessionService = sessionService;
    }

    public void processMessage(JSONObject json, int userId, int roomId) {
        Room room = roomStateService.getRoom(roomId);

        String command = json.getString("command");
        switch (command) {
            case "ready": {
                boolean value = json.getBooleanValue("value");
                room.playerReady(userId);
                sessionService.sendToRoomExceptUser(room, userId, MessageWrapper.updateMemberMessage(room));

                // 如果所有人都准备就开始游戏
                if (room.canStartGame()) {
                    room.startGame();

                    // 向所有人发送开始游戏信息
                    sessionService.sendToRoom(room, MessageWrapper.startGameMessage());
                }

                break;
            }
        }
    }
}
