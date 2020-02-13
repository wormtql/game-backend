package com.example.game.service;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class RoomIdService {
    private Set<Integer> inuse = new HashSet<>();
    private Set<Integer> vacant = new HashSet<>();

    public RoomIdService() {
        for (int i = 0; i < 10000; i++) {
            vacant.add(i);
        }
    }

    public int nextId() {
        if (vacant.isEmpty()) {
            return -1;
        }
        int temp = vacant.iterator().next();
        vacant.remove(temp);
        inuse.add(temp);
        return temp;
    }

    public boolean returnId(int id) {
        if (!inuse.contains(id)) {
            return false;
        }

        inuse.remove(id);
        vacant.add(id);

        return true;
    }
}
