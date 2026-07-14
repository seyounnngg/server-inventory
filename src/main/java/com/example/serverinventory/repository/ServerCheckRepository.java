package com.example.serverinventory.repository;

import com.example.serverinventory.model.ServerCheck;

import java.util.List;

public interface ServerCheckRepository {
    void save(ServerCheck check);

    List<ServerCheck> findByServerId(Long serverId);

    ServerCheck findLatestByServerId(Long serverId);
}