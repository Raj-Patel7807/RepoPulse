package com.repopulse.dao;

import com.repopulse.model.RepoFile;
import com.repopulse.database.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepoFileDAO {
    private final Connection conn;

    public RepoFileDAO() {
        this.conn = DBConnection.getConnection();
    }

    public void createFile(RepoFile file) throws SQLException {
        String sql = """
            INSERT INTO repo_files (repository_id, file_name, file_path, is_binary)
            VALUES (?, ?, ?, ?)
            """;

        try(PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, file.getRepoId());
            ps.setString(2, file.getFileName());
            ps.setString(3, file.getFilePath());
            ps.setBoolean(4, file.isBinary());
            ps.executeUpdate();

            try(ResultSet rs = ps.getGeneratedKeys()) {
                if(rs.next()) {
                    file.setFileId(rs.getLong(1));
                }
            }
        }
    }

    public RepoFile getFileById(long fileId) throws SQLException {
        String sql = "SELECT * FROM repo_files WHERE file_id = ?";

        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, fileId);

            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    return mapRowToRepoFile(rs);
                }
            }
        }
        return null;
    }

    public List<RepoFile> getFilesByRepo(long repoId) throws SQLException {
        String sql = "SELECT * FROM repo_files WHERE repository_id = ?";

        List<RepoFile> files = new ArrayList<>();

        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, repoId);

            try(ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    files.add(mapRowToRepoFile(rs));
                }
            }
        }
        return files;
    }

    public void updateFile(RepoFile file) throws SQLException {
        String sql = """
            UPDATE repo_files
            SET file_name = ?, file_path = ?, is_binary = ?
            WHERE file_id = ?
            """;

        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, file.getFileName());
            ps.setString(2, file.getFilePath());
            ps.setBoolean(3, file.isBinary());
            ps.setLong(4, file.getFileId());
            ps.executeUpdate();
        }
    }

    public void deleteFile(long fileId) throws SQLException {
        String sql = "DELETE FROM repo_files WHERE file_id = ?";

        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, fileId);
            ps.executeUpdate();
        }
    }

    private RepoFile mapRowToRepoFile(ResultSet rs) throws SQLException {
        RepoFile file = new RepoFile(
                rs.getLong("file_id"),
                rs.getLong("repository_id"),
                rs.getString("file_name"),
                rs.getString("file_path"),
                rs.getBoolean("is_binary")
        );

        return file;
    }
}
