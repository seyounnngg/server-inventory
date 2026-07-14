package com.example.serverinventory.service;

import com.example.serverinventory.model.Server;
import com.example.serverinventory.repository.ServerRepository;

import java.util.List;

public class ServerService {
    private final ServerRepository repository;

    public ServerService(ServerRepository repository) {
        this.repository = repository;
    }

    public void registerServer(String hostname, String ipAddress, String type, Long ownerId) {
        Server server = new Server(hostname, ipAddress, type, ownerId);
        repository.save(server);
    }

    public List<Server> getAllServers() {
        return repository.findAll();
    }

    public Server getServerById(Long id) {
        return repository.findById(id);
    }

    public void updateServer(Long id, String hostname, String ipAddress, String type, Long ownerId) {
        Server existingServer = repository.findById(id);
        if (existingServer == null) {
            return;
        }
        existingServer.setHostname(hostname);
        existingServer.setIpAddress(ipAddress);
        existingServer.setType(type);
        existingServer.setOwnerId(ownerId);
        repository.update(existingServer);
    }

    public void deleteServer(Long id) {
        Server existingServer = repository.findById(id);
        if (existingServer == null) {
            return;
        }
        repository.delete(id);
    }

    public List<Server> getServerByType(String type) {
        return repository.findByType(type);
    }

    public List<Server> getServerByOwnerId(Long id) {
        return repository.findByOwnerId(id);
    }
}
