package com.repopulse.release.service;

import com.repopulse.release.dao.RepoReleaseDAO;
import com.repopulse.release.model.RepoRelease;

import java.sql.Timestamp;
import java.util.List;

public class RepoReleaseService {
    private final RepoReleaseDAO dao;

    public RepoReleaseService() {
        this.dao = new RepoReleaseDAO();
    }

    public void createRelease(long repositoryId, long tagId, String title, String notes, long createdByUserId) {
        RepoRelease release = new RepoRelease();
        release.setRepositoryId(repositoryId);
        release.setTagId(tagId);
        release.setReleaseTitle(title);
        release.setReleaseNotes(notes);
        release.setCreatedByUserId(createdByUserId);
        release.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        this.dao.createRelease(release);
    }

    public List<RepoRelease> getReleasesByRepo(long repositoryId) {
        return this.dao.getReleasesByRepo(repositoryId);
    }
}
