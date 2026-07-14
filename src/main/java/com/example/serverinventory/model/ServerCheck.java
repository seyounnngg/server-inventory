package com.example.serverinventory.model;

import java.time.LocalDateTime;

public class ServerCheck {
    private Long id;
    private Long serverId;
    private LocalDateTime checkedAt;
    private String status;

    public ServerCheck(Long id, Long serverId, LocalDateTime checkedAt, String status) {
        this.id = id;
        this.serverId = serverId;
        this.checkedAt = checkedAt;
        this.status = status;
    }

    public ServerCheck(Long serverId, LocalDateTime checkedAt, String status) {
        this.serverId = serverId;
        this.checkedAt = checkedAt;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public LocalDateTime getCheckedAt() {
        return checkedAt;
    }

    public void setCheckedAt(LocalDateTime checkedAt) {
        this.checkedAt = checkedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
