package com.repopulse.repository.dao;

import com.repopulse.infra.exception.DataAccessException;
import com.repopulse.infra.database.DBConnection;
import com.repopulse.repository.model.*;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.sql.Connection;
import java.sql.ResultSet;
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

        } catch(SQLException e) {
            throw new RuntimeException("Creating Repo Failed, Check RepoName", e);
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
            throw new DataAccessException("Failed to load repository.", e);
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
            throw new DataAccessException("Failed to list repositories for user.", e);
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
            throw new DataAccessException("Failed to list repositories.", e);
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
            throw new DataAccessException("Failed to update repository.", e);
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
            throw new DataAccessException("Failed to delete repository.", e);
        }
    }

    public void createClone(RepoClone clone) {
        String sql = """
                INSERT INTO repo_clones (repository_id, cloned_by_user_id, clone_type)
                VALUES (?, ?, ?)
                """;

        try(PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, clone.getRepositoryId());
            stmt.setLong(2, clone.getClonedByUserId());
            stmt.setString(3, clone.getCloneType());

            int affectedRow = stmt.executeUpdate();
            if(affectedRow == 0) {
                throw new SQLException("Creating Clone failed, no rows affected.");
            }

            try(ResultSet rs = stmt.getGeneratedKeys()) {
                if(rs.next()) {
                    clone.setCloneId(rs.getLong(1));
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<RepoClone> getClonesByRepoId(long repoId) {
        List<RepoClone> clones = new ArrayList<>();

        String sql = """
                SELECT * FROM repo_clones WHERE repository_id = ?
                """;

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, repoId);

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                clones.add(mapResultSetToRepoClone(rs));
            }
        } catch(SQLException e) {
            throw new DataAccessException("Failed to load clones for repository.", e);
        }

        return clones;
    }

    public List<RepoClone> getClonesByUserId(long userId) {
        List<RepoClone> clones = new ArrayList<>();

        String sql = """
                SELECT * FROM repo_clones WHERE cloned_by_user_id = ?
                """;

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                clones.add(mapResultSetToRepoClone(rs));
            }
        } catch(SQLException e) {
            throw new DataAccessException("Failed to load clones for user.", e);
        }

        return clones;
    }

    public void addCollaborator(RepoCollaborator collaborator) {
        String sql = """
                INSERT INTO repo_collaborators (repository_id, user_id, access_role)
                VALUES (?, ?, ?)
                """;

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, collaborator.getRepositoryId());
            stmt.setLong(2, collaborator.getUserId());
            stmt.setString(3, collaborator.getAccessRole());

            int affectedRows = stmt.executeUpdate();
            if(affectedRows == 0) {
                throw new SQLException("Adding collaborator failed, no rows affected.");
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error adding collaborator", e);
        }
    }

    public void removeCollaborator(long repoId, long userId) {
        String sql = "DELETE FROM repo_collaborators WHERE repository_id = ? AND user_id = ?";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, repoId);
            stmt.setLong(2, userId);

            int affectedRows = stmt.executeUpdate();
            if(affectedRows == 0) {
                throw new SQLException("Removing collaborator failed, no rows affected.");
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error removing collaborator", e);
        }
    }

    public void updateAccessRole(long repoId, long userId, String accessRole) {
        String sql = "UPDATE repo_collaborators SET access_role = ? WHERE repository_id = ? AND user_id = ?";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, accessRole);
            stmt.setLong(2, repoId);
            stmt.setLong(3, userId);

            int affectedRows = stmt.executeUpdate();
            if(affectedRows == 0) {
                throw new SQLException("Updating collaborator role failed, no rows affected.");
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error updating collaborator role", e);
        }
    }

    public List<RepoCollaborator> getCollaboratorsByRepo(long repoId) {
        List<RepoCollaborator> collaborators = new ArrayList<>();

        String sql = "SELECT * FROM repo_collaborators WHERE repository_id = ? ORDER BY joined_at";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, repoId);

            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    collaborators.add(mapResultSetToCollaborator(rs));
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error fetching collaborators", e);
        }

        return collaborators;
    }

    public List<RepoCollaborator> getCollaboratorsByUser(long userId) {
        List<RepoCollaborator> collaborators = new ArrayList<>();

        String sql = "SELECT * FROM repo_collaborators WHERE user_id = ? ORDER BY joined_at";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);

            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    collaborators.add(mapResultSetToCollaborator(rs));
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error fetching user's collaborations", e);
        }

        return collaborators;
    }

    public String getCollaboratorRole(long repoId, long userId) {
        String sql = "SELECT access_role FROM repo_collaborators WHERE repository_id = ? AND user_id = ?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, repoId);
            stmt.setLong(2, userId);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    return rs.getString("access_role");
                }
            }
        } catch(SQLException e) {
            throw new DataAccessException("Failed to check collaborator role.", e);
        }
        return null;
    }

    public void addStar(RepoStar star) {
        String sql = "INSERT INTO repo_stars (user_id, repository_id, starred_at) VALUES (?, ?, ?)";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, star.getUserId());
            stmt.setLong(2, star.getRepositoryId());
            stmt.setTimestamp(3, star.getStarredAt());
            stmt.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException("Error adding RepoStar", e);
        }
    }

    public void removeStar(long userId, long repositoryId) {
        String sql = "DELETE FROM repo_stars WHERE user_id = ? AND repository_id = ?";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, repositoryId);
            stmt.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException("Error removing RepoStar", e);
        }
    }

    public List<Repository> getStarredRepos(long userId) {
        List<Repository> repos = new ArrayList<>();

        String sql = """
                SELECT r.*
                FROM repo_stars rs
                JOIN repositories r ON rs.repository_id = r.repository_id
                WHERE rs.user_id = ?;
                """;

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);

            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    repos.add(mapResultSetToRepository(rs));
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error Fetching Starred Repos", e);
        }

        return repos;
    }

    public int getStarCountByRepository(long repositoryId) {
        String sql = "SELECT COUNT(*) FROM repo_stars WHERE repository_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, repositoryId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching star count", e);
        }

        return 0;
    }

    public List<RepoStar> getStarsByRepository(long repositoryId) {
        List<RepoStar> stars = new ArrayList<>();

        String sql = "SELECT * FROM repo_stars WHERE repository_id = ?";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, repositoryId);

            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    RepoStar star = new RepoStar();

                    star.setUserId(rs.getLong("user_id"));
                    star.setRepositoryId(rs.getLong("repository_id"));
                    star.setStarredAt(rs.getTimestamp("starred_at"));
                    stars.add(star);
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error fetching RepoStars", e);
        }
        return stars;
    }

    public void createTag(RepoTag tag) {
        String sql = "INSERT INTO repo_tags (repository_id, commit_id, tag_name, tag_description) VALUES (?, ?, ?, ?)";

        try(PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, tag.getRepositoryId());
            stmt.setLong(2, tag.getCommitId());
            stmt.setString(3, tag.getTagName());
            stmt.setString(4, tag.getTagDescription());

            int affectedRows = stmt.executeUpdate();
            if(affectedRows == 0) {
                throw new SQLException("Creating tag failed, no rows affected.");
            }

            try(ResultSet rs = stmt.getGeneratedKeys()) {
                if(rs.next()) {
                    tag.setTagId(rs.getLong(1));
                }
            }

        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public RepoTag getTagById(long tagId) {
        String sql = "SELECT * FROM repo_tags WHERE tag_id = ?";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, tagId);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                return mapResultSetToRepoTag(rs);
            }
        } catch(SQLException e) {
            throw new DataAccessException("Failed to load tag.", e);
        }
        return null;
    }

    public List<RepoTag> getTagsByRepositoryId(long repositoryId) {
        List<RepoTag> tags = new ArrayList<>();

        String sql = "SELECT * FROM repo_tags WHERE repository_id = ?";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, repositoryId);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                tags.add(mapResultSetToRepoTag(rs));
            }
        } catch(SQLException e) {
            throw new DataAccessException("Failed to load tags for repository.", e);
        }
        return tags;
    }

    public void addOrUpdateWatcher(RepoWatcher watcher) {
        String sql = """
            INSERT INTO repo_watchers (user_id, repository_id, watch_level, watched_at)
            VALUES (?, ?, ?, ?)
            ON CONFLICT (user_id, repository_id)
            DO UPDATE SET watch_level = EXCLUDED.watch_level,
                          watched_at = EXCLUDED.watched_at
        """;

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, watcher.getUserId());
            stmt.setLong(2, watcher.getRepositoryId());
            stmt.setString(3, watcher.getWatchLevel().name());
            stmt.setTimestamp(4, watcher.getWatchedAt());

            stmt.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException("Error adding/updating RepoWatcher", e);
        }
    }

    public List<RepoWatcher> getWatchers(long repositoryId) {
        List<RepoWatcher> watchers = new ArrayList<>();

        String sql = "SELECT * FROM repo_watchers WHERE repository_id = ?";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, repositoryId);

            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    RepoWatcher w = new RepoWatcher();
                    w.setUserId(rs.getLong("user_id"));
                    w.setRepositoryId(rs.getLong("repository_id"));
                    w.setWatchLevel(RepoWatcher.WatchLevel.valueOf(rs.getString("watch_level")));
                    w.setWatchedAt(rs.getTimestamp("watched_at"));
                    watchers.add(w);
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error fetching RepoWatchers", e);
        }
        return watchers;
    }

    private RepoTag mapResultSetToRepoTag(ResultSet rs) throws SQLException {
        RepoTag tag = new RepoTag();

        tag.setTagId(rs.getLong("tag_id"));
        tag.setRepositoryId(rs.getLong("repository_id"));
        tag.setCommitId(rs.getLong("commit_id"));
        tag.setTagName(rs.getString("tag_name"));
        tag.setTagDescription(rs.getString("tag_description"));
        tag.setCreatedAt(rs.getTimestamp("created_at"));

        return tag;
    }

    private RepoCollaborator mapResultSetToCollaborator(ResultSet rs) {
        try {
            RepoCollaborator collaborator = new RepoCollaborator();

            collaborator.setRepositoryId(rs.getLong("repository_id"));
            collaborator.setUserId(rs.getLong("user_id"));
            collaborator.setAccessRole(rs.getString("access_role"));

            return collaborator;

        } catch(SQLException e) {
            throw new RuntimeException("Error mapping RepoCollaborator", e);
        }
    }

    private RepoClone mapResultSetToRepoClone(ResultSet rs) {
        try {
            RepoClone repoClone = new RepoClone();

            repoClone.setCloneId(rs.getLong("clone_id"));
            repoClone.setRepositoryId(rs.getLong("repository_id"));
            repoClone.setClonedByUserId(rs.getLong("cloned_by_user_id"));
            repoClone.setCloneType(rs.getString("clone_type"));

            return repoClone;

        } catch(SQLException e) {
            throw new RuntimeException("Error mapping clones", e);
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
