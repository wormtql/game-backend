package com.example.game.service;

import com.example.game.game_spec.GameSpecFactory;
import com.example.game.game_state.GameStateFactory;
import com.example.game.model.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class RoomStateService {
    @Autowired
    private RoomIdService roomIdService;
    @Autowired
    private GameSpecFactory gameSpecFactory;
    @Autowired
    private GameStateFactory gameStateFactory;

    private Map<Integer, Room> rooms = new HashMap<>();

    public Room createRoom(int id, String type, String password) {
        log.info("creating room type " + type + " pwd " + password);

        Room room = new Room(id, gameSpecFactory.getGameSpec(type), gameStateFactory.getNewGameState(type));
        room.setPassword(password);

        rooms.put(id, room);

        log.info("room " + id + " created");

        return room;
    }

    public boolean isRoomExist(int id) {
        return rooms.containsKey(id);
    }

    public Room getRoom(int id) {
        if (isRoomExist(id)) {
            return rooms.get(id);
        }

        return null;
    }

    public void dropRoom(int roomId) {
        log.info("dropping room " + roomId);
        rooms.remove(roomId);
        roomIdService.returnId(roomId);
    }
}
