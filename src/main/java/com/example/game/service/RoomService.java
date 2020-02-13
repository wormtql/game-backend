package com.example.game.service;

import com.example.game.dto.request.ApplyRoomRequestObject;
import com.example.game.dto.request.JoinRoomRequestObject;
import com.example.game.dto.response.ApplyRoomResponse;
import com.example.game.dto.response.CommonResponse;
import com.example.game.model.Room;
import com.example.game.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class RoomService {
    private RoomIdService roomIdService;
    private RoomStateService roomStateService;

    @Autowired
    public RoomService(RoomIdService roomIdService, RoomStateService roomStateService) {
        this.roomIdService = roomIdService;
        this.roomStateService = roomStateService;
    }

    public ApplyRoomResponse applyRoom(ApplyRoomRequestObject requestObject, HttpServletRequest request) {
        // 验证身份
        String jwt = request.getHeader("Authorization");
        Claims claims = JwtUtil.verifyJwt(jwt);
        if (claims == null) {
            return new ApplyRoomResponse(0, 401, "未授权");
        }

        String type = requestObject.getType();
        String password = requestObject.getPassword();

        int id = roomIdService.nextId();
        if (id == -1) {
            return new ApplyRoomResponse(0, 1000, "没有可用房间号");
        }

        if (roomStateService.createRoom(id, type, password)) {
            return new ApplyRoomResponse(id, 200, "ok");
        }

        return new ApplyRoomResponse(0, 1000, "无法创建房间");
    }

    public CommonResponse joinRoom(HttpServletRequest request, JoinRoomRequestObject requestObject) {
        // 验证身份
        String jwt = request.getHeader("Authorization");
        Claims claims = JwtUtil.verifyJwt(jwt);
        if (claims == null) {
            return new CommonResponse("未授权", 401);
        }

        int userId = (Integer) claims.get("userId");

        int roomId = requestObject.getRoomId();
        String password = requestObject.getPassword();

        // 验证room是否存在
        if (!roomStateService.isRoomExist(roomId)) {
            return new CommonResponse("无法加入房间", 1000);
        }

        // 验证是否满员
        Room room = roomStateService.getRoom(roomId);
        if (room.getPlayerCount() >= room.getExpectedPlayerCount()) {
            return new CommonResponse("房间已满", 1000);
        }

        // 验证密码
        if (!room.getPassword().equals(password)) {
            return new CommonResponse("密码错误", 1000);
        }

        // 可以加入
        room.addPlayer(userId);
        return new CommonResponse("ok", 200);
    }
}
