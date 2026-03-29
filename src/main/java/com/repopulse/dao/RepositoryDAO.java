package com.repopulse.dao;

import com.repopulse.model.Repository;
import com.repopulse.database.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepositoryDAO {

    private final Connection conn;

    public RepositoryDAO() {
        this.conn = DBConnection.getConnection();
    }

    public void createRepository(Repository repo) {
        String sql = "INSERT INTO repositories (repository_name, repository_description, owner_user_id, visibility_type, parent_repository_id, forked_from_commit_id) VALUES (?, ?, ?, ?, ?, ?)";

        try(PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, repo.getRepoName());
            ps.setString(2, repo.getRepoDesc());
            ps.setLong(3, repo.getRepoOwnerUserId());
            ps.setString(4, repo.getRepoVisibilityType());

            if(repo.getParentRepositoryId() != null) {
                ps.setLong(5, repo.getParentRepositoryId());
            } else {
                ps.setNull(5, Types.BIGINT);
            }

            if(repo.getForkedFromCommitId() != null) {
                ps.setLong(6, repo.getForkedFromCommitId());
            } else {
                ps.setNull(6, Types.BIGINT);
            }

            int affectedRows = ps.executeUpdate();
            if(affectedRows == 0) {
                throw new SQLException("Creating repository failed.");
            }

            try(ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if(generatedKeys.next()) {
                    repo.setRepoId(generatedKeys.getLong(1));
                }
            }

            System.out.println("Repository created successfully!");

        } catch(SQLException e) {
            if(e.getMessage().contains("unique")) {
                System.out.println("Repository name already exists for this user!");
            } else {
                e.printStackTrace();
            }
        }
    }

    public Repository getRepositoryById(long repoId) {
        String sql = "SELECT * FROM repositories WHERE repository_id=? AND is_deleted=FALSE";

        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, repoId);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                return mapResultSetToRepository(rs);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Repository> getRepositoriesByUser(long userId) {
        List<Repository> repos = new ArrayList<>();

        String sql = "SELECT * FROM repositories WHERE owner_user_id=? AND is_deleted=FALSE";

        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                repos.add(mapResultSetToRepository(rs));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return repos;
    }

    public List<Repository> getAllRepositories(String visibility) {
        List<Repository> repos = new ArrayList<>();

        String sql = "SELECT * FROM repositories WHERE visibility_type=? AND is_deleted=FALSE";

        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, visibility);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                repos.add(mapResultSetToRepository(rs));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return repos;
    }

    public void updateRepository(Repository repo) {
        String sql = "UPDATE repositories SET repository_name=?, repository_description=?, visibility_type=?, default_branch_id=?, updated_at=CURRENT_TIMESTAMP WHERE repository_id=? AND is_deleted=FALSE";

        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, repo.getRepoName());
            ps.setString(2, repo.getRepoDesc());
            ps.setString(3, repo.getRepoVisibilityType());

            if(repo.getDefaultBranchId() != null) {
                ps.setLong(4, repo.getDefaultBranchId());
            } else {
                ps.setNull(4, Types.BIGINT);
            }
            ps.setLong(5, repo.getRepoId());

            ps.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void softDeleteRepository(long repoId, Long deletedByUserId) {
        String sql = "UPDATE repositories SET is_deleted=TRUE, deleted_at=CURRENT_TIMESTAMP, deleted_by_user_id=? WHERE repository_id=?";

        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            if(deletedByUserId != null) {
                ps.setLong(1, deletedByUserId);
            } else {
                ps.setNull(1, Types.BIGINT);
            }
            ps.setLong(2, repoId);

            ps.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private Repository mapResultSetToRepository(ResultSet rs) throws SQLException {
        Repository repo = new Repository();

        repo.setRepoId(rs.getLong("repository_id"));
        repo.setRepoName(rs.getString("repository_name"));
        repo.setRepoDesc(rs.getString("repository_description"));
        repo.setRepoOwnerUserId(rs.getLong("owner_user_id"));
        repo.setRepoVisibilityType(rs.getString("visibility_type"));

        long defaultBranch = rs.getLong("default_branch_id");
        repo.setDefaultBranchId(rs.wasNull() ? null : defaultBranch);

        long parentRepo = rs.getLong("parent_repository_id");
        repo.setParentRepositoryId(rs.wasNull() ? null : parentRepo);

        long forkedCommit = rs.getLong("forked_from_commit_id");
        repo.setForkedFromCommitId(rs.wasNull() ? null : forkedCommit);

        return repo;
    }
}
