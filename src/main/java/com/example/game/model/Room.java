package com.example.game.model;

import com.example.game.model.game_state.GameState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Room {
    // 房间号
    private int id;
    // 房间类型
    private String type = "";
    // 期望玩家数
    private int expectedPlayerCount = 0;
    // 当前玩家数
    private int playerCount = 0;
    // 活跃连接数
    private int activeConnCount = 0;
    // 是否已经开始游戏
    private boolean active = false;
    // 房间密码
    private String password = "";
    // 已经加入的玩家id
    private List<Integer> players;

    // 抽象的游戏状态
    private GameState gameState = null;

    public Room(int id) {
        this.id = id;
        this.players = new ArrayList<>();
    }
    
    public boolean addPlayer(int id) {
        if (playerCount < expectedPlayerCount) {
            players.add(id);
            playerCount++;
            return true;
        }
        return false;
    }
}
