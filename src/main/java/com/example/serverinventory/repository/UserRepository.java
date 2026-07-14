package com.example.serverinventory.repository;

import com.example.serverinventory.model.User;

import java.util.List;

public interface UserRepository {

    void save(User user);

    List<User> findAll();

    User findById(Long id);

    void update(User user);

    void delete(Long id);
}
