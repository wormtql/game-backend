package com.example.game.game_spec;

import com.example.game.model.PlayerState;
import com.example.game.model.Room;

import java.util.List;
import java.util.Map;

public interface AbstractGameSpec {
    // 玩家数
    int getExpectedPlayerCount();

    // 游戏名称(类型)
    String getGameType();

    // 有人退出且房间没有解散(还有至少一个玩家)时调用此方法
    void onPlayerQuit(int role, Room room);

    void onPlayerSurrender(int surrenderRole, Room room);

    int nextPlayer(Map<Integer, PlayerState> players, int currentPlayer);

    int firstPlayer(Map<Integer, PlayerState> players, int round);

    void assignRole(Map<Integer, PlayerState> players, int round);
}
