package com.example.serverinventory.repository;

import com.example.serverinventory.model.Server;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ServerFileRepository implements ServerRepository {

    private final String filePath = "servers.csv";
    private final List<Server> servers;
    private Long nextId;

    public ServerFileRepository() {
        this.servers = loadFromFile();
        this.nextId = calculateNextId();
    }

    private List<Server> loadFromFile() {
        List<Server> loadedServers = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return loadedServers;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Long id = Long.parseLong(parts[0]);
                String hostname = parts[1];
                String ipAddress = parts[2];
                String type = parts[3];
                Long ownerId = Long.parseLong(parts[4]);
                loadedServers.add(new Server(id, hostname, ipAddress, type, ownerId));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadedServers;
    }

    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Server server : servers) {
                writer.write(server.getId() + "," + server.getHostname() + ","
                        + server.getIpAddress() + "," + server.getType() + "," + server.getOwnerId());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Long calculateNextId() {
        return servers.stream()
                .map(Server::getId)
                .max(Comparator.naturalOrder())
                .orElse(0L) + 1;
    }

    @Override
    public void save(Server server) {
        server.setId(nextId);
        nextId++;
        servers.add(server);
        saveToFile();
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
        saveToFile();
    }

    @Override
    public void delete(Long id) {
        servers.removeIf(server -> server.getId().equals(id));
        saveToFile();
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
