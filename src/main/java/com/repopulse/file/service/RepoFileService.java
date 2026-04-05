package com.repopulse.file.service;

import com.repopulse.file.dao.RepoFileDAO;
import com.repopulse.file.model.FileDiff;
import com.repopulse.file.model.FileVersion;
import com.repopulse.file.model.RepoFile;

import java.sql.SQLException;
import java.util.List;

public class RepoFileService {
    private final RepoFileDAO fileDAO;

    public RepoFileService() {
        this.fileDAO = new RepoFileDAO();
    }

    public void addFile(RepoFile file) {
        try {
            fileDAO.createFile(file);
            System.out.println("File added successfully with ID: " + file.getFileId());
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public RepoFile getFile(long fileId) {
        try {
            return fileDAO.getFileById(fileId);
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<RepoFile> getFilesByRepo(long repoId) {
        try {
            return fileDAO.getFilesByRepo(repoId);
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return List.of();
    }

    public void updateFile(RepoFile file) {
        try {
            fileDAO.updateFile(file);
            System.out.println("File updated successfully!");
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFile(long fileId) {
        try {
            fileDAO.deleteFile(fileId);
            System.out.println("File deleted successfully!");
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void createFileVersion(long fileId, long commitId, String contentHash, long fileSizeBytes, String changeType) {
        validateChangeType(changeType);

        FileVersion version = new FileVersion();

        version.setFileId(fileId);
        version.setCommitId(commitId);
        version.setContentHash(contentHash);
        version.setFileSizeBytes(fileSizeBytes);
        version.setChangeType(changeType);

        fileDAO.createFileVersion(version);
    }

    public FileVersion getFileVersion(long fileVersionId) {
        return fileDAO.getFileVersionById(fileVersionId);
    }

    public List<FileVersion> getFileVersions(long fileId) {
        return fileDAO.getVersionsByFileId(fileId);
    }

    public void generateDiff(long oldVersionId, long newVersionId, String diffContent, String diffFormat) {
        validateDiffFormat(diffFormat);

        FileDiff diff = new FileDiff();

        diff.setOldFileVersionId(oldVersionId);
        diff.setNewFileVersionId(newVersionId);
        diff.setDiffContent(diffContent);
        diff.setDiffFormat(diffFormat);

        fileDAO.createDiff(diff);
    }

    public FileDiff getDiff(long oldVersionId, long newVersionId) {
        return fileDAO.getDiffByVersionIds(oldVersionId, newVersionId);
    }

    public List<FileDiff> getDiffsForVersion(long fileVersionId) {
        return fileDAO.getDiffsByFileVersion(fileVersionId);
    }

    private void validateDiffFormat(String diffFormat) {
        if(!diffFormat.equalsIgnoreCase("UNIFIED") && !diffFormat.equalsIgnoreCase("CONTEXT")) {
            throw new IllegalArgumentException("Invalid diff format: " + diffFormat);
        }
    }

    private void validateChangeType(String changeType) {
        switch(changeType.toUpperCase()) {
            case "ADDED", "MODIFIED", "DELETED", "RENAMED" -> {}
            default -> throw new IllegalArgumentException("Invalid change type: " + changeType);
        }
    }
}
