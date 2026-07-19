package com.example.serverinventory.repository;

import com.example.serverinventory.model.User;

import java.util.List;

public interface UserRepository {

    void save(User user);

    List<User> findAll();

    User findById(Long id);

    User findByUsername(String username);

    void update(User user);

    void delete(Long id);
}
