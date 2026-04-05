package com.repopulse.file.dao;

import com.repopulse.file.model.FileDiff;
import com.repopulse.file.model.FileVersion;
import com.repopulse.infra.database.DBConnection;
import com.repopulse.file.model.RepoFile;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
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

    public void createDiff(FileDiff diff) {
        String sql = """
                INSERT INTO file_diffs (old_file_version_id, new_file_version_id, diff_content, diff_format)
                VALUES (?, ?, ?, ?)
                """;

        try(PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, diff.getOldFileVersionId());
            stmt.setLong(2, diff.getNewFileVersionId());
            stmt.setString(3, diff.getDiffContent());
            stmt.setString(4, diff.getDiffFormat());

            int affectedRows = stmt.executeUpdate();
            if(affectedRows == 0) {
                throw new SQLException("Creating FileDiff failed, no rows affected.");
            }

            try(ResultSet rs = stmt.getGeneratedKeys()) {
                if(rs.next()) {
                    diff.setDiffId(rs.getLong(1));
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error creating FileDiff", e);
        }
    }

    public FileDiff getDiffByVersionIds(long oldVersionId, long newVersionId) {
        String sql = """
                SELECT * FROM file_diffs
                WHERE old_file_version_id = ? AND new_file_version_id = ?
                """;

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, oldVersionId);
            stmt.setLong(2, newVersionId);

            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    return mapResultSetToFileDiff(rs);
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error fetching FileDiff", e);
        }
        return null;
    }

    public List<FileDiff> getDiffsByFileVersion(long fileVersionId) {
        List<FileDiff> diffs = new ArrayList<>();

        String sql = """
                SELECT * FROM file_diffs
                WHERE old_file_version_id = ? OR new_file_version_id = ?
                """;

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, fileVersionId);
            stmt.setLong(2, fileVersionId);

            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    diffs.add(mapResultSetToFileDiff(rs));
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error fetching FileDiffs", e);
        }

        return diffs;
    }

    private FileDiff mapResultSetToFileDiff(ResultSet rs) {
        try {
            FileDiff diff = new FileDiff();

            diff.setDiffId(rs.getLong("diff_id"));
            diff.setOldFileVersionId(rs.getLong("old_file_version_id"));
            diff.setNewFileVersionId(rs.getLong("new_file_version_id"));
            diff.setDiffContent(rs.getString("diff_content"));
            diff.setDiffFormat(rs.getString("diff_format"));

            return diff;

        } catch(SQLException e) {
            throw new RuntimeException("Error mapping FileDiff", e);
        }
    }

    public void createFileVersion(FileVersion version) {
        String sql = """
                INSERT INTO file_versions (file_id, commit_id, content_hash, file_size_bytes, change_type)
                VALUES (?, ?, ?, ?, ?)
                """;

        try(PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, version.getFileId());
            stmt.setLong(2, version.getCommitId());
            stmt.setString(3, version.getContentHash());
            stmt.setLong(4, version.getFileSizeBytes());
            stmt.setString(5, version.getChangeType());

            int affectedRows = stmt.executeUpdate();
            if(affectedRows == 0) {
                throw new SQLException("Creating FileVersion failed, no rows affected.");
            }

            try(ResultSet rs = stmt.getGeneratedKeys()) {
                if(rs.next()) {
                    version.setFileVersionId(rs.getLong(1));
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error creating FileVersion", e);
        }
    }

    public FileVersion getFileVersionById(long fileVersionId) {
        String sql = "SELECT * FROM file_versions WHERE file_version_id = ?";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, fileVersionId);

            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    return mapResultSetToFileVersion(rs);
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error fetching FileVersion", e);
        }
        return null;
    }

    public List<FileVersion> getVersionsByFileId(long fileId) {
        List<FileVersion> versions = new ArrayList<>();

        String sql = "SELECT * FROM file_versions WHERE file_id = ? ORDER BY created_at ASC";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, fileId);

            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    versions.add(mapResultSetToFileVersion(rs));
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error fetching FileVersions", e);
        }

        return versions;
    }

    private FileVersion mapResultSetToFileVersion(ResultSet rs) {
        try {
            FileVersion version = new FileVersion();

            version.setFileVersionId(rs.getLong("file_version_id"));
            version.setFileId(rs.getLong("file_id"));
            version.setCommitId(rs.getLong("commit_id"));
            version.setContentHash(rs.getString("content_hash"));
            version.setFileSizeBytes(rs.getLong("file_size_bytes"));
            version.setChangeType(rs.getString("change_type"));

            return version;

        } catch(SQLException e) {
            throw new RuntimeException("Error mapping FileVersion", e);
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
