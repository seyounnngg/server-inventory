package com.example.serverinventory.repository;

import com.example.serverinventory.model.Server;

import java.util.List;


public interface ServerRepository {

    void save(Server server);

    List<Server> findAll();

    Server findById(Long id);

    void update(Server server);

    void delete(Long id);

    List<Server> findByType(String type);

    List<Server> findByOwnerId(Long ownerId);
}