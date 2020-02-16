package com.example.game.service;

import com.alibaba.fastjson.JSONObject;
import com.example.game.model.PlayerState;
import com.example.game.model.Room;
import com.example.game.util.MessageWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GameService {
    private RoomStateService roomStateService;
    private SessionService sessionService;

    @Autowired
    public GameService(RoomStateService roomStateService, SessionService sessionService) {
        this.roomStateService = roomStateService;
        this.sessionService = sessionService;
    }

    public void notifyNextPlayer(Room room) {
        int userId = room.getCurrentPlayerId();
        sessionService.sendToUser(userId, MessageWrapper.yourTurnMessage());
    }

    public void notifyGameOver(Room room, int winnerRole) {
        for (int u : room.getPlayers().keySet()) {
            if (winnerRole == room.getPlayers().get(u).getRole()) {
                sessionService.sendToUser(u, MessageWrapper.gameOverMessage(winnerRole));
            } else {
                sessionService.sendToUser(u, MessageWrapper.gameOverMessage(winnerRole));
            }
        }
    }

    public void processMessage(JSONObject json, int userId, int roomId) {
        Room room = roomStateService.getRoom(roomId);

        String command = json.getString("command");
        switch (command) {
            case "ready": {
                if (room.isActive()) {
                    log.error("game already started, but someone is prepared");
                }

                room.playerReady(userId);
                sessionService.sendToRoomExceptUser(room, userId, MessageWrapper.updateMemberMessage(room));

                // 如果所有人都准备就开始游戏
                if (room.canStartGame()) {
                    room.startGame();

                    // 向所有人发送开始游戏信息
                    for (int u : room.getPlayers().keySet()) {
                        sessionService.sendToUser(u, MessageWrapper.startGameMessage(room.getPlayers().get(u).getRole()));
                    }

                    this.notifyNextPlayer(room);
                }

                break;
            }
            case "play": {
                PlayerState ps = room.getPlayers().get(userId);
                if (ps.getRole() != room.getCurrentPlayerState().getRole()) {
                    return;
                }

                JSONObject play = json.getJSONObject("play");
                room.getGameState().updateGameState(play);

                // 向其他人发送更新信息
                sessionService.sendToRoomExceptUser(room, userId, json);

                int winner = room.getGameState().winner();
                if (winner == -1) {
                    // 还没分出胜负
                    room.nextPlayer();
                    this.notifyNextPlayer(room);
                } else {
                    // 已经分出胜负
                    log.info("game over " + winner);

                    // 通知所有玩家
                    notifyGameOver(room, winner);

                    // 更新room状态
                    room.endGame();
                }

                break;
            }
            case "surrender": {
                int surrenderRole = json.getInteger("role");
                log.info("role " + surrenderRole + " surrendered");

                room.getGameSpec().onPlayerSurrender(surrenderRole, room);

                room.endGame();

                break;
            }
        }
    }
}
