package com.example.serverinventory.model;

public class Server {

    private Long id;
    private String hostname;
    private String ipAddress;
    private String type;
    private Long ownerId;

    public Server(Long id, String hostname, String ipAddress, String type, Long ownerId) {
        this.id = id;
        this.hostname = hostname;
        this.ipAddress = ipAddress;
        this.type = type;
        this.ownerId = ownerId;
    }

    public Server(String hostname, String ipAddress, String type, Long ownerId) {
        this.hostname = hostname;
        this.ipAddress = ipAddress;
        this.type = type;
        this.ownerId = ownerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}
