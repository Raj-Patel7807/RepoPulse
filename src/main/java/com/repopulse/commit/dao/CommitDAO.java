package com.repopulse.commit.dao;

import com.repopulse.infra.exception.DataAccessException;
import com.repopulse.commit.model.Commit;
import com.repopulse.infra.database.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class CommitDAO {

    private final Connection conn;

    public CommitDAO() {
        this.conn = DBConnection.getConnection();
    }

    public long createCommit(Commit commit) {
        String sql = "INSERT INTO commits (repository_id, author_user_id, parent_commit_id, commit_hash, commit_message) VALUES (?, ?, ?, ?, ?)";

        try(PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, commit.getRepoId());
            ps.setLong(2, commit.getAuthorUserId());

            if(commit.getParentCommitId() == null) {
                ps.setNull(3, Types.BIGINT);
            } else {
                ps.setLong(3, commit.getParentCommitId());
            }

            ps.setString(4, commit.getCommitHash());
            ps.setString(5, commit.getCommitMessage());

            ps.executeUpdate();

            try(ResultSet keys = ps.getGeneratedKeys()) {
                if(keys.next()) {
                    commit.setCommitId(keys.getLong(1));
                    return commit.getCommitId();
                }
            }

        } catch(SQLException e) {
            throw new RuntimeException("Error creating commit", e);
        }

        return -1;
    }

    public Commit getCommitById(long commitId) {
        String sql = "SELECT * FROM commits WHERE commit_id = ?";

        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, commitId);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                return mapResultSetToCommit(rs);
            }
        } catch(SQLException e) {
            throw new DataAccessException("Failed to load commit.", e);
        }
        return null;
    }

    public List<Commit> getCommitsByRepo(long repoId) {
        List<Commit> commits = new ArrayList<>();

        String sql = "SELECT * FROM commits WHERE repository_id = ? ORDER BY committed_at DESC";

        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, repoId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                commits.add(mapResultSetToCommit(rs));
            }
        } catch(SQLException e) {
            throw new DataAccessException("Failed to list commits for repository.", e);
        }
        return commits;
    }

    public List<Commit> getCommitsByAuthor(long authorUserId) {
        List<Commit> commits = new ArrayList<>();

        String sql = "SELECT * FROM commits WHERE author_user_id = ? ORDER BY committed_at DESC";

        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, authorUserId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                commits.add(mapResultSetToCommit(rs));
            }
        } catch(SQLException e) {
            throw new DataAccessException("Failed to list commits for author.", e);
        }

        return commits;
    }

    public Commit getLatestCommit(long repoId) {
        String sql = "SELECT * FROM commits WHERE repository_id = ? ORDER BY committed_at DESC LIMIT 1";

        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, repoId);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                return mapResultSetToCommit(rs);
            }
        } catch(SQLException e) {
            throw new DataAccessException("Failed to load latest commit.", e);
        }
        return null;
    }

    public void deleteCommit(long commitId) {
        String sql = "DELETE FROM commits WHERE commit_id = ?";

        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, commitId);
            ps.executeUpdate();
        } catch(SQLException e) {
            throw new DataAccessException("Failed to delete commit.", e);
        }
    }

    private Commit mapResultSetToCommit(ResultSet rs) throws SQLException {
        Commit commit = new Commit();

        commit.setCommitId(rs.getLong("commit_id"));
        commit.setRepoId(rs.getLong("repository_id"));
        commit.setAuthorUserId(rs.getLong("author_user_id"));
        commit.setParentCommitId((Long) rs.getObject("parent_commit_id"));
        commit.setCommitHash(rs.getString("commit_hash"));
        commit.setCommitMessage(rs.getString("commit_message"));
        commit.setCommittedAt(rs.getTimestamp("committed_at"));

        return commit;
    }
}
