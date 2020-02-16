package com.example.game.game_spec;

import com.example.game.model.PlayerState;
import com.example.game.model.Room;
import com.example.game.util.MessageWrapper;
import com.example.game.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public void onPlayerQuit(int role, Room room) {
        // 对手直接胜利
        for (int u : room.getPlayers().keySet()) {
            int r = room.getPlayers().get(u).getRole();
            if (r != role) {
                sessionService.sendToUser(u, MessageWrapper.gameOverMessage(r));
            }
        }
    }

    @Override
    public void onPlayerSurrender(int role, Room room) {
        onPlayerQuit(role, room);
    }

    @Override
    public int firstPlayer(Map<Integer, PlayerState> players, int round) {
        List<Integer> temp = new ArrayList<>(players.keySet());
        return temp.get(round % 2);
    }

    @Override
    public int nextPlayer(Map<Integer, PlayerState> players, int currentPlayer) {
        for (int u : players.keySet()) {
            if (u != currentPlayer) {
                return u;
            }
        }
        return players.keySet().iterator().next();
    }

    @Override
    public void assignRole(Map<Integer, PlayerState> players, int round) {
        int i = 0;
        for (int u : players.keySet()) {
            if (round % 2 == i) {
                players.get(u).setRole(1);
            } else {
                players.get(u).setRole(2);
            }
            i++;
        }
    }
}
