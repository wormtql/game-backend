package com.example.game.service;

import com.example.game.model.Room;
import com.example.game.model.game_state.HexGameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RoomStateService {
    private Map<Integer, Room> rooms = new HashMap<>();
    private Logger logger = LoggerFactory.getLogger(RoomStateService.class);

    public boolean createRoom(int id, String type, String password) {
        logger.info("creating room type " + type + " pwd " + password);

        Room room = new Room(id);
        room.setPassword(password);

        if (type.equals("hex")) {
            room.setType("hex");
            room.setExpectedPlayerCount(2);
            room.setGameState(new HexGameState());

            rooms.put(id, room);

            logger.info("room " + id + " created");

            return true;
        }

        return false;
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
}
