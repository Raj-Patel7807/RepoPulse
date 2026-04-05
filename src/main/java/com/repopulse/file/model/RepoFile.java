package com.repopulse.file.model;

import java.sql.Timestamp;

public class RepoFile {
    private long fileId;
    private long repoId;
    private String fileName;
    private String filePath;
    private boolean isBinary;
    private Timestamp createdAt;

    public RepoFile() {

    }

    public RepoFile(long fileId, long repoId, String fileName, String filePath, boolean isBinary) {
        this.fileId = fileId;
        this.repoId = repoId;
        this.fileName = fileName;
        this.filePath = filePath;
        this.isBinary = isBinary;
    }

    public long getFileId() {
        return this.fileId;
    }
    public void setFileId(long fileId) {
        this.fileId = fileId;
    }

    public long getRepoId() {
        return this.repoId;
    }
    public void setRepoId(long repoId) {
        this.repoId = repoId;
    }

    public String getFileName() {
        return this.fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return this.filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isBinary() {
        return this.isBinary;
    }
    public void setBinary(boolean isBinary) {
        this.isBinary = isBinary;
    }

    public Timestamp getCreatedAt() {
        return this.createdAt;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
