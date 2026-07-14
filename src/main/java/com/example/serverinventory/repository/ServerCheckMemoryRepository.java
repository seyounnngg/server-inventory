package com.example.serverinventory.repository;

import com.example.serverinventory.model.ServerCheck;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ServerCheckMemoryRepository implements ServerCheckRepository {

    private final List<ServerCheck> serverChecks = new ArrayList<>();
    private Long nextId = 1L;

    @Override
    public void save(ServerCheck check) {
        check.setId(nextId);
        nextId++;
        serverChecks.add(check);
    }

    @Override
    public List<ServerCheck> findByServerId(Long serverId) {
        return serverChecks.stream()
                .filter(check -> check.getServerId().equals(serverId))
                .toList();
    }

    @Override
    public ServerCheck findLatestByServerId(Long serverId) {
        return serverChecks.stream()
                .filter(check -> check.getServerId().equals(serverId))
                .max(Comparator.comparing(ServerCheck::getCheckedAt))
                .orElse(null);
    }
}
