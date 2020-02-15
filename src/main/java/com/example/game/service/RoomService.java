package com.example.game.service;

import com.example.game.dto.request.ApplyRoomRequestObject;
import com.example.game.dto.request.JoinRoomRequestObject;
import com.example.game.dto.response.ApplyRoomResponse;
import com.example.game.dto.response.CommonResponse;
import com.example.game.model.Room;
import com.example.game.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@Slf4j
public class RoomService {
    private RoomIdService roomIdService;
    private RoomStateService roomStateService;

    @Autowired
    public RoomService(RoomIdService roomIdService, RoomStateService roomStateService) {
        this.roomIdService = roomIdService;
        this.roomStateService = roomStateService;
    }

    public Room applyRoom(int userId, String type, String password) {
        log.info("applying room");

        // 是否有可用roomId
        int id = roomIdService.nextId();
        if (id == -1) {
            log.info("no vacant room");
            return null;
        }

        // 创建room
        Room room = roomStateService.createRoom(id, type, password);
        if (room == null) {
            log.info("cannot create room");
            return null;
        }

        log.info("created room " + id);
        return room;
    }

    public Room joinRoom(int userId, String username, int roomId, String password) {
        // 验证room是否存在
        if (!roomStateService.isRoomExist(roomId)) {
            log.info("room not exist while joining room");
            return null;
        }

        // 验证是否满员
        Room room = roomStateService.getRoom(roomId);
        if (room.getPlayerCount() >= room.getExpectedPlayerCount()) {
            log.info("room full");
            return null;
        }

        // 验证密码
        if (!room.getPassword().equals("") && !room.getPassword().equals(password)) {
            log.info("password wrong while joining room");
            return null;
        }

        // 可以加入
        room.addPlayer(userId, username);
        log.info("player " + userId + " joined room " + roomId);
        return room;
    }
}
