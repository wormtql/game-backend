package com.example.game.game_spec;

import com.example.game.model.Room;

import java.util.List;

public interface AbstractGameSpec {
    // 玩家数
    int getExpectedPlayerCount();

    // 游戏名称(类型)
    String getGameType();

    // 有人退出且房间没有解散(还有至少一个玩家)时调用此方法
    void onPlayerQuit(int userId, Room room);

    int nextPlayer(List<Integer> players, int currentPlayer);

    default int firstPlayer(List<Integer> players, int round) {
        return players.get(round % 2);
    }


}
