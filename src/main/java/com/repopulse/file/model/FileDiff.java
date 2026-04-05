package com.repopulse.file.model;

import java.sql.Timestamp;

public class FileDiff {
    private long diffId;
    private long oldFileVersionId;
    private long newFileVersionId;
    private String diffContent;
    private String diffFormat;
    private Timestamp generatedAt;

    public FileDiff() {

    }

    public long getDiffId() {
        return this.diffId;
    }
    public void setDiffId(long diffId) {
        this.diffId = diffId;
    }

    public long getOldFileVersionId() {
        return this.oldFileVersionId;
    }
    public void setOldFileVersionId(long oldFileVersionId) {
        this.oldFileVersionId = oldFileVersionId;
    }

    public long getNewFileVersionId() {
        return this.newFileVersionId;
    }
    public void setNewFileVersionId(long newFileVersionId) {
        this.newFileVersionId = newFileVersionId;
    }

    public String getDiffContent() {
        return this.diffContent;
    }
    public void setDiffContent(String diffContent) {
        this.diffContent = diffContent;
    }

    public String getDiffFormat() {
        return this.diffFormat;
    }
    public void setDiffFormat(String diffFormat) {
        this.diffFormat = diffFormat;
    }

    public Timestamp getGeneratedAt() {
        return this.generatedAt;
    }
    public void setGeneratedAt(Timestamp generatedAt) {
        this.generatedAt = generatedAt;
    }
}
