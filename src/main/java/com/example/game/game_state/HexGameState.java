package com.example.game.game_state;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.Stack;

@Slf4j
public class HexGameState implements AbstractGameState {
    private int toPlay = 1;
    private int[][] board = new int[11][11];
    private int[][] dir = new int[6][2];

    public HexGameState() {
        refresh();

        dir[0][0] = 0;
        dir[0][1] = 1;

        dir[1][0] = 1;
        dir[1][1] = 0;

        dir[2][0] = 0;
        dir[2][1] = -1;

        dir[3][0] = -1;
        dir[3][1] = 0;

        dir[4][0] = -1;
        dir[4][1] = 1;

        dir[5][0] = 1;
        dir[5][1] = -1;
    }

    private boolean isRoleWon(int role) {
        Stack<Integer> st = new Stack<>();
        boolean[][] vis = new boolean[11][11];

        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                vis[i][j] = false;
            }
        }

        if (role == 1) {
            for (int i = 0; i < 11; i++) {
                if (board[0][i] == 1) {
                    st.add(i);
                    vis[0][i] = true;
                }
            }
        } else {
            for (int i = 0; i < 11; i++) {
                if (board[i][0] == 2) {
                    st.add(i * 11);
                    vis[i][0] = true;
                }
            }
        }

        while (!st.empty()) {
            int p = st.pop();
            for (int i = 0; i < 6; i++) {
                int nx = p / 11 + this.dir[i][0];
                int ny = p % 11 + this.dir[i][1];
                if (nx >= 0 && nx < 11 && ny >= 0 && ny < 11 && !vis[nx][ny]) {
                    if (board[nx][ny] == role) {
                        if ((nx == 10 && role == 1) || (ny == 10 && role == 2)) {
                            return true;
                        }
                        st.push(nx * 11 + ny);
                        vis[nx][ny] = true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void updateGameState(JSONObject move) {
        int x = move.getInteger("x");
        int y = move.getInteger("y");
        int role = move.getInteger("role");
        if (x >= 0 && x < 11 && y >= 0 && y < 11) {
            board[x][y] = role;

//            log.info("move " + x + " " + y + " " + role);
        }
    }

    @Override
    public int winner() {
        if (isRoleWon(1)) {
            return 1;
        } else if (isRoleWon(2)) {
            return 2;
        }
        return -1;
    }

    @Override
    public void refresh() {
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                this.board[i][j] = 0;
            }
        }
    }
}
