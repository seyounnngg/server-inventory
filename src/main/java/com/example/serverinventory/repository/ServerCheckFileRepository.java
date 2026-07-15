package com.example.serverinventory.repository;

import com.example.serverinventory.model.ServerCheck;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ServerCheckFileRepository implements ServerCheckRepository {

    private final String filePath = "server_checks.csv";
    private final List<ServerCheck> serverChecks;
    private Long nextId;

    public ServerCheckFileRepository() {
        this.serverChecks = loadFromFile();
        this.nextId = calculateNextId();
    }

    private List<ServerCheck> loadFromFile() {
        List<ServerCheck> loadedChecks = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return loadedChecks;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Long id = Long.parseLong(parts[0]);
                Long serverId = Long.parseLong(parts[1]);
                LocalDateTime checkedAt = LocalDateTime.parse(parts[2]);
                String status = parts[3];
                loadedChecks.add(new ServerCheck(id, serverId, checkedAt, status));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadedChecks;
    }

    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (ServerCheck check : serverChecks) {
                writer.write(check.getId() + "," + check.getServerId() + ","
                        + check.getCheckedAt() + "," + check.getStatus());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Long calculateNextId() {
        return serverChecks.stream()
                .map(ServerCheck::getId)
                .max(Comparator.naturalOrder())
                .orElse(0L) + 1;
    }

    @Override
    public void save(ServerCheck check) {
        check.setId(nextId);
        nextId++;
        serverChecks.add(check);
        saveToFile();
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
