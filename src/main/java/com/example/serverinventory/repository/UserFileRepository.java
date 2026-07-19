package com.example.serverinventory.repository;

import com.example.serverinventory.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class UserFileRepository implements UserRepository {

    private final String filePath = "users.csv";
    private final List<User> users;
    private Long nextId;

    public UserFileRepository() {
        this.users = loadFromFile();
        this.nextId = calculateNextId();
    }

    private List<User> loadFromFile() {
        List<User> loadedUsers = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return loadedUsers;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Long id = Long.parseLong(parts[0]);
                String name = parts[1];
                String team = parts[2];
                String username = parts[3];
                String password = parts[4];
                loadedUsers.add(new User(id, name, team, username, password));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadedUsers;
    }

    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (User user : users) {
                writer.write(user.getId() + "," + user.getName() + "," + user.getTeam()
                        + "," + user.getUsername() + "," + user.getPassword());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Long calculateNextId() {
        return users.stream()
                .map(User::getId)
                .max(Comparator.naturalOrder())
                .orElse(0L) + 1;
    }

    @Override
    public void save(User user) {
        user.setId(nextId);
        nextId++;
        users.add(user);
        saveToFile();
    }

    @Override
    public List<User> findAll() {
        return users;
    }

    @Override
    public User findById(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public User findByUsername(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void update(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.getId())) {
                users.set(i, user);
            }
        }
        saveToFile();
    }

    @Override
    public void delete(Long id) {
        users.removeIf(user -> user.getId().equals(id));
        saveToFile();
    }
}
