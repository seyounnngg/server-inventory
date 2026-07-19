package com.example.serverinventory.model;

public class User {
    private Long id;
    private String name;
    private String team;
    private String username;
    private String password;

    public User(String name, String team, String username, String password) {
        this.name = name;
        this.team = team;
        this.username = username;
        this.password = password;
    }

    public User(Long id, String name, String team, String username, String password) {
        this.id = id;
        this.name = name;
        this.team = team;
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}