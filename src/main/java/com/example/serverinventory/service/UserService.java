package com.example.serverinventory.service;

import com.example.serverinventory.model.User;
import com.example.serverinventory.repository.UserRepository;

import java.util.List;

public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public void registerUser(String name, String team) {
        User user = new User(name, team);
        repository.save(user);
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User getUserById(Long id) {
        return repository.findById(id);
    }

    public void updateUser(Long id, String name, String team) {
        User existingUser = repository.findById(id);
        if (existingUser == null) {
            return;
        }
        existingUser.setName(name);
        existingUser.setTeam(team);
        repository.update(existingUser);
    }

    public void deleteUser(Long id) {
        User existingUser = repository.findById(id);
        if (existingUser == null) {
            return;
        }
        repository.delete(id);
    }
}
