package com.example.serverinventory.repository;

import com.example.serverinventory.model.Server;

import java.util.ArrayList;
import java.util.List;

public class ServerMemoryRepository implements ServerRepository {

    private final List<Server> servers = new ArrayList<>();
    private Long nextId = 1L;

    @Override
    public void save(Server server) {
        server.setId(nextId);
        nextId++;
        servers.add(server);
    }

    @Override
    public List<Server> findAll() {
        return servers;
    }

    @Override
    public Server findById(Long id) {
        return servers.stream()
                .filter(server -> server.getId().equals(id))
                .findFirst()
                .orElse(null);

    }

    @Override
    public void update(Server server) {
        for (int i = 0; i < servers.size(); i++) {
            if (servers.get(i).getId().equals(server.getId())) {
                servers.set(i, server);
            }
        }
    }

    @Override
    public void delete(Long id) {
        servers.removeIf(server -> server.getId().equals(id));
    }

    @Override
    public List<Server> findByType(String type) {
        return servers.stream()
                .filter(server -> server.getType().equals(type))
                .toList();
    }

    @Override
    public List<Server> findByOwnerId(Long ownerId) {
        return servers.stream()
                .filter(server -> server.getOwnerId().equals(ownerId))
                .toList();
    }
}
