package com.example.game.controller;

import com.example.game.dto.request.ApplyRoomRequestObject;
import com.example.game.dto.request.JoinRoomRequestObject;
import com.example.game.dto.response.ApplyRoomResponse;
import com.example.game.dto.response.CommonResponse;
import com.example.game.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RoomController {
    SimpMessageSendingOperations simpMessageSendingOperations;
    RoomService roomService;

    @Autowired
    public RoomController(SimpMessageSendingOperations simpMessageSendingOperations,
                          RoomService roomService) {
        this.simpMessageSendingOperations = simpMessageSendingOperations;
        this.roomService = roomService;
    }

    @GetMapping("/room/apply")
    public ApplyRoomResponse applyRoom(@RequestBody ApplyRoomRequestObject requestObject,
                                       HttpServletRequest request) {
        return roomService.applyRoom(requestObject, request);
    }

    @GetMapping("/room/join")
    public CommonResponse joinRoom(HttpServletRequest request,
                                   @RequestBody JoinRoomRequestObject requestObject) {
        return roomService.joinRoom(request, requestObject);
    }

    @MessageMapping("/play")
    public void play(StompHeaderAccessor headerAccessor) {
//        simpMessageSendingOperations.convertAndSendToUser("1", "/topic/", 1);

    }
}
