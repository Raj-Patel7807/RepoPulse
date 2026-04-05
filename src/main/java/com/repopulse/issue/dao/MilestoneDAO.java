package com.repopulse.issue.dao;

import com.repopulse.infra.database.DBConnection;
import com.repopulse.issue.model.Milestone;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class MilestoneDAO {
    private final Connection conn;

    public MilestoneDAO() {
        this.conn = DBConnection.getConnection();
    }

    public void createMilestone(Milestone m) {
        String sql = """
            INSERT INTO milestones (repository_id, title, description, due_date, status, created_at, closed_at)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try(PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, m.getRepositoryId());
            stmt.setString(2, m.getTitle());
            stmt.setString(3, m.getDescription());
            stmt.setDate(4, m.getDueDate());
            stmt.setString(5, m.getStatus().name());
            stmt.setTimestamp(6, m.getCreatedAt());
            if(m.getClosedAt() != null) stmt.setTimestamp(7, m.getClosedAt());
            else stmt.setNull(7, Types.TIMESTAMP);

            stmt.executeUpdate();
            try(ResultSet rs = stmt.getGeneratedKeys()) {
                if(rs.next()) m.setMilestoneId(rs.getLong(1));
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error creating milestone", e);
        }
    }

    public List<Milestone> getMilestonesByRepo(long repositoryId) {
        List<Milestone> milestones = new ArrayList<>();

        String sql = "SELECT * FROM milestones WHERE repository_id = ?";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, repositoryId);

            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    Milestone m = new Milestone();
                    m.setMilestoneId(rs.getLong("milestone_id"));
                    m.setRepositoryId(rs.getLong("repository_id"));
                    m.setTitle(rs.getString("title"));
                    m.setDescription(rs.getString("description"));
                    m.setDueDate(rs.getDate("due_date"));
                    m.setStatus(Milestone.Status.valueOf(rs.getString("status")));
                    m.setCreatedAt(rs.getTimestamp("created_at"));
                    m.setClosedAt(rs.getTimestamp("closed_at"));
                    milestones.add(m);
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error fetching milestones", e);
        }
        return milestones;
    }
}
