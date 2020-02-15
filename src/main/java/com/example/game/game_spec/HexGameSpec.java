package com.example.game.game_spec;

import com.example.game.model.Room;
import com.example.game.util.MessageWrapper;
import com.example.game.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class HexGameSpec implements AbstractGameSpec {
    private SessionService sessionService;

    @Autowired
    public HexGameSpec(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public int getExpectedPlayerCount() {
        return 2;
    }

    @Override
    public String getGameType() {
        return "hex";
    }

    @Override
    public void onPlayerQuit(int userId, Room room) {
        if (room.isActive()) {
            // 已经开始游戏的时候有人退出
            // 对手直接胜利
            for (int u : room.getPlayers().keySet()) {
                if (u != userId) {
                    sessionService.sendToUser(u, MessageWrapper.gameOverMessage(true));
                }
            }
        } else {
            // 游戏还没有开始
            // 通知其他玩家有人退出
            sessionService.sendToRoomExceptUser(room, userId, MessageWrapper.playerQuitMessage(userId));
        }
    }

    @Override
    public int nextPlayer(List<Integer> players, int currentPlayer) {
        if (players.get(0) == currentPlayer) {
            return players.get(1);
        } else {
            return players.get(0);
        }
    }
}
