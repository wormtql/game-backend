package com.example.game.model;

import com.example.game.game_spec.AbstractGameSpec;
import com.example.game.game_state.AbstractGameState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class Room {
    // 房间号
    private int id;
    // 房间密码
    private String password = "";

    // 当前玩家数
    private int playerCount = 0;
    // 是否已经开始游戏
    private boolean active = false;

    // 已经加入的玩家id以及准备状态
    private Map<Integer, Boolean> players = new HashMap<>();
    private Map<Integer, String> playerName = new HashMap<>();

    // 抽象的游戏状态
    private AbstractGameState gameState;
    private AbstractGameSpec gameSpec;

    // 当前是第几局
    private int round = 0;
    // 当前应该谁落子
    private int currentPlayerId = -1;

    public Room(int id, AbstractGameSpec gameSpec, AbstractGameState gameState) {
        this.id = id;

        this.gameSpec = gameSpec;
        this.gameState = gameState;
    }

    public int getExpectedPlayerCount() {
        return gameSpec.getExpectedPlayerCount();
    }

    public String getGameName() {
        return gameSpec.getGameType();
    }

    public boolean addPlayer(int id, String username) {
        if (playerCount < gameSpec.getExpectedPlayerCount()) {
            players.put(id, false);
            playerName.put(id, username);
            playerCount++;
            return true;
        }
        return false;
    }

    public boolean playerReady(int id) {
        if (players.containsKey(id)) {
            players.put(id, true);
            return true;
        }
        return false;
    }

    public void playerExit(int userId) {
        log.info("player " + userId + " exiting room " + id);
        playerCount -= 1;
        players.remove(userId);
    }

    public boolean canStartGame() {
        if (getPlayerCount() == getExpectedPlayerCount()) {
            for (int u : this.players.keySet()) {
                if (!this.players.get(u)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public void startGame() {
        this.active = true;
    }
}
