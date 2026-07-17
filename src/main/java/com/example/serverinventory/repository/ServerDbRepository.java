package com.example.serverinventory.repository;

import com.example.serverinventory.model.Server;
import com.example.serverinventory.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServerDbRepository implements ServerRepository {
    private final Connection connection;

    public ServerDbRepository() {
        this.connection = DBConnection.getInstance().getConnection();
    }

    @Override
    public void save(Server server) {
        String sql = "INSERT INTO server (hostname, ip_address, type, owner_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, server.getHostname());
            stmt.setString(2, server.getIpAddress());
            stmt.setString(3, server.getType());
            stmt.setLong(4, server.getOwnerId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Server> findAll() {
        List<Server> servers = new ArrayList<>();
        String sql = "SELECT * FROM server";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                servers.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return servers;
    }

    @Override
    public Server findById(Long id) {
        String sql = "SELECT * FROM server WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);

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

    @Override
    public void update(Server server) {
        String sql = "UPDATE server SET hostname = ?, ip_address = ?, type = ?, owner_id = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, server.getHostname());
            stmt.setString(2, server.getIpAddress());
            stmt.setString(3, server.getType());
            stmt.setLong(4, server.getOwnerId());
            stmt.setLong(5, server.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM server WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Server> findByType(String type) {
        List<Server> servers = new ArrayList<>();

        String sql = "SELECT * FROM server WHERE type = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, type);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    servers.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return servers;
    }

    @Override
    public List<Server> findByOwnerId(Long ownerId) {
        List<Server> servers = new ArrayList<>();

        String sql = "SELECT * FROM server WHERE owner_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, ownerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    servers.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return servers;
    }

    private Server mapRow(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String hostname = rs.getString("hostname");
        String ipAddress = rs.getString("ip_address");
        String type = rs.getString("type");
        Long ownerId = rs.getLong("owner_id");
        return new Server(id, hostname, ipAddress, type, ownerId);
    }
}
