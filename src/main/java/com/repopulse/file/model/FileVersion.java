package com.repopulse.file.model;

import java.sql.Timestamp;

public class FileVersion {
    private long fileVersionId;
    private long fileId;
    private long commitId;
    private String contentHash;
    private long fileSizeBytes;
    private String changeType;
    private Timestamp createdAt;

    public FileVersion() {

    }

    public long getFileVersionId() {
        return this.fileVersionId;
    }

    public void setFileVersionId(long fileVersionId) {
        this.fileVersionId = fileVersionId;
    }

    public long getFileId() {
        return this.fileId;
    }

    public void setFileId(long fileId) {
        this.fileId = fileId;
    }

    public long getCommitId() {
        return this.commitId;
    }

    public void setCommitId(long commitId) {
        this.commitId = commitId;
    }

    public String getContentHash() {
        return this.contentHash;
    }

    public void setContentHash(String contentHash) {
        this.contentHash = contentHash;
    }

    public long getFileSizeBytes() {
        return this.fileSizeBytes;
    }

    public void setFileSizeBytes(long fileSizeBytes) {
        this.fileSizeBytes = fileSizeBytes;
    }

    public String getChangeType() {
        return this.changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public Timestamp getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
