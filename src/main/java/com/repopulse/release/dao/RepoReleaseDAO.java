package com.repopulse.release.dao;

import com.repopulse.infra.database.DBConnection;
import com.repopulse.release.model.RepoRelease;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepoReleaseDAO {
    private final Connection conn;

    public RepoReleaseDAO() {
        this.conn = DBConnection.getConnection();
    }

    public void createRelease(RepoRelease release) {
        String sql = "INSERT INTO repo_releases (repository_id, tag_id, release_title, release_notes, created_by_user_id, created_at) VALUES (?, ?, ?, ?, ?, ?)";

        try(PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, release.getRepositoryId());
            stmt.setLong(2, release.getTagId());
            stmt.setString(3, release.getReleaseTitle());
            stmt.setString(4, release.getReleaseNotes());
            stmt.setLong(5, release.getCreatedByUserId());
            stmt.setTimestamp(6, release.getCreatedAt());
            stmt.executeUpdate();

            try(ResultSet rs = stmt.getGeneratedKeys()) {
                if(rs.next()) release.setReleaseId(rs.getLong(1));
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error creating release", e);
        }
    }

    public List<RepoRelease> getReleasesByRepo(long repositoryId) {
        List<RepoRelease> releases = new ArrayList<>();

        String sql = "SELECT * FROM repo_releases WHERE repository_id = ?";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, repositoryId);

            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    RepoRelease r = new RepoRelease();
                    r.setReleaseId(rs.getLong("release_id"));
                    r.setRepositoryId(rs.getLong("repository_id"));
                    r.setTagId(rs.getLong("tag_id"));
                    r.setReleaseTitle(rs.getString("release_title"));
                    r.setReleaseNotes(rs.getString("release_notes"));
                    r.setCreatedByUserId(rs.getLong("created_by_user_id"));
                    r.setCreatedAt(rs.getTimestamp("created_at"));
                    releases.add(r);
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error fetching releases", e);
        }
        return releases;
    }
}
