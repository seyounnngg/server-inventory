package com.example.serverinventory.repository;

import com.example.serverinventory.model.ServerCheck;
import com.example.serverinventory.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServerCheckDbRepository implements ServerCheckRepository {

    private final Connection connection;

    public ServerCheckDbRepository() {
        this.connection = DBConnection.getInstance().getConnection();
    }


    @Override
    public void save(ServerCheck check) {
        String sql = "INSERT INTO server_check (server_id, checked_at, status) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, check.getServerId());
            stmt.setObject(2, check.getCheckedAt());
            stmt.setString(3, check.getStatus());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ServerCheck> findByServerId(Long serverId) {
        List<ServerCheck> checks = new ArrayList<>();

        String sql = "SELECT * FROM server_check WHERE server_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, serverId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    checks.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return checks;
    }


    @Override
    public ServerCheck findLatestByServerId(Long serverId) {
        String sql = "SELECT * FROM server_check WHERE server_id = ? ORDER BY checked_at DESC LIMIT 1";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, serverId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ServerCheck mapRow(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        Long serverId = rs.getLong("server_id");
        LocalDateTime checkAt = rs.getObject("checked_at", LocalDateTime.class);
        String status = rs.getString("status");
        return new ServerCheck(id, serverId, checkAt, status);
    }
}
