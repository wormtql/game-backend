package com.example.game.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.game.config.CustomSpringConfigurator;
import com.example.game.model.Room;
import com.example.game.service.*;
import com.example.game.util.JwtUtil;
import com.example.game.util.MessageWrapper;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;

@Slf4j
@Component
@ServerEndpoint(value = "/websocket", configurator = CustomSpringConfigurator.class)
public class MyWebSocketServer {
    private SessionService sessionService;

    private RoomStateService roomStateService;

    private GameService gameService;

    private RoomService roomService;

    @Autowired
    public MyWebSocketServer(SessionService sessionService,
                             RoomStateService roomStateService,
                             GameService gameService,
                             RoomService roomService) {
        this.sessionService = sessionService;
        this.roomStateService = roomStateService;
        this.gameService = gameService;
        this.roomService = roomService;
    }

    @OnOpen
    public void onOpen(Session session) {
        log.info("session open " + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        log.info("session close " + session.getId());

        if (!sessionService.isSessionExist(session)) {
            return;
        }

        int roomId = sessionService.getRoomIdBySessionId(session.getId());
        int userId = sessionService.getUserIdBySessionId(session.getId());

        Room room = roomStateService.getRoom(roomId);
        if (room.getPlayerCount() == 1) {
            // 当前退出能够造成房间销毁
            roomStateService.dropRoom(roomId);
        } else {
            // 不能造成房间销毁
            // 调用特定游戏的玩家退出回调

            if (room.isActive()) {
                room.getGameSpec().onPlayerQuit(room.getPlayers().get(userId).getRole(), room);
                // 更新房间状态
                room.playerExit(userId);
                // 重置游戏
                room.endGame();
            } else {
                room.playerExit(userId);
            }
        }

        // 删除会话
        sessionService.removeSession(session);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        JSONObject obj = JSON.parseObject(message);

        String command = obj.getString("command");


        if (sessionService.isSessionExist(session)) {
            // session 已经加入管理
            int userId = sessionService.getUserIdBySessionId(session.getId());
            int roomId = sessionService.getRoomIdBySessionId(session.getId());

            if (roomStateService.isRoomExist(roomId)) {
                gameService.processMessage(obj, userId, roomId);
            }
        } else {
            // session 还没有加入管理
            if (command.equals("create")) {
                // 创建房间
                String token = obj.getString("token");
                Claims claims = JwtUtil.verifyJwt(token);
                if (claims == null) {
                    sessionService.sendToSession(session, MessageWrapper.errorMessage("未授权"));
                    return;
                }

                int userId = Integer.parseInt(claims.get("userId", String.class));
                String username = claims.getSubject();

                String type = obj.getString("type");
                String password = obj.getString("password");

                Room room = roomService.applyRoom(userId, type, password);
                if (room == null) {
                    sessionService.sendToSession(session, MessageWrapper.errorMessage("无法创建房间"));
                    return;
                }
                int roomId = room.getId();

                room.addPlayer(userId, username);

//                String roomToken = JwtUtil.createRoomToken(userId, roomId);
                sessionService.addSession(session, userId, roomId);
                try {
                    sessionService.sendToUser(userId, MessageWrapper.roomIdMessage(roomId));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (command.equals("join")) {
                // 加入房间
                String token = obj.getString("token");
                Claims claims = JwtUtil.verifyJwt(token);
                if (claims == null) {
                    sessionService.sendToSession(session, MessageWrapper.errorMessage("未授权"));
                    return;
                }

                int userId = Integer.parseInt(claims.get("userId", String.class));
                String username = claims.getSubject();

                String password = obj.getString("password");
                int roomId = obj.getIntValue("roomId");

                Room room = roomService.joinRoom(userId, username, roomId, password);
                if (room == null) {
                    sessionService.sendToSession(session, MessageWrapper.errorMessage("无法加入房间"));
                    return;
                }

//                String roomToken = JwtUtil.createRoomToken(userId, roomId);
                sessionService.addSession(session, userId, roomId);
                try {
                    sessionService.sendToUser(userId, MessageWrapper.roomIdMessage(roomId));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 向刚加入的人发送已经存在的玩家信息,向已经在的人发送更新消息
                for (int u : room.getPlayers().keySet()) {
                    sessionService.sendToUser(u, MessageWrapper.updateMemberMessage(room));
                }
            }
        }
    }
}
