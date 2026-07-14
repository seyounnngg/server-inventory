package com.example.serverinventory.service;

import com.example.serverinventory.model.ServerCheck;
import com.example.serverinventory.repository.ServerCheckRepository;

import java.time.LocalDateTime;
import java.util.List;

public class ServerCheckService {

    private final ServerCheckRepository repository;

    public ServerCheckService(ServerCheckRepository repository) {
        this.repository = repository;
    }

    public void recordCheck(Long serverId, String status) {
        ServerCheck check = new ServerCheck(serverId, LocalDateTime.now(), status);
        repository.save(check);
    }

    public List<ServerCheck> getCheckHistory(Long serverId) {
        return repository.findByServerId(serverId);
    }

    public ServerCheck getLatestCheck(Long serverId) {
        return repository.findLatestByServerId(serverId);
    }
}
