package com.example.serverinventory.model;

public class User {
    private Long id;
    private String name;
    private String team;

    public User(Long id, String name, String team) {
        this.id = id;
        this.name = name;
        this.team = team;
    }

    public User(String name, String team) {
        this.name = name;
        this.team = team;
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
}
