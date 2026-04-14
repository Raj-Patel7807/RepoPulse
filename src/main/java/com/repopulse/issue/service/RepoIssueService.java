package com.repopulse.issue.service;

import com.repopulse.issue.dao.RepoIssueDAO;
import com.repopulse.issue.model.IssueLabel;
import com.repopulse.issue.model.RepoIssue;

import java.sql.Timestamp;
import java.util.List;

public class RepoIssueService {

    private final RepoIssueDAO repoIssueDAO;

    public RepoIssueService() {
        this.repoIssueDAO = new RepoIssueDAO();
    }

    public void createIssue(long repositoryId, long createdByUserId, String title, String description, String priority) {

        RepoIssue issue = new RepoIssue();

        issue.setRepositoryId(repositoryId);
        issue.setCreatedByUserId(createdByUserId);
        issue.setTitle(title);
        issue.setDescription(description);
        issue.setStatus(validateStatus("OPEN"));
        issue.setPriority(validatePriority(priority));

        repoIssueDAO.createRepoIssue(issue);
    }

    public void closeIssue(long issueId, long repoId, long userId) {
        repoIssueDAO.closeIssue(issueId, repoId, userId);
    }

    public RepoIssue getIssue(long issueId) {
        return repoIssueDAO.getRepoIssueById(issueId);
    }

    public List<RepoIssue> getIssuesByRepository(long repositoryId) {
        return repoIssueDAO.getIssuesByRepositoryId(repositoryId);
    }

    public void assignLabelToIssue(long issueId, long labelId) {
        repoIssueDAO.assignLabel(issueId, labelId);
    }

    public List<Long> getLabelsOfIssue(long issueId) {
        return repoIssueDAO.getLabelIdsForIssue(issueId);
    }

    public void createLabel(long repositoryId, String name, String color) {
        IssueLabel label = new IssueLabel();
        label.setRepositoryId(repositoryId);
        label.setLabelName(name);
        label.setLabelColor(color);
        label.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        repoIssueDAO.createLabel(label);
    }

    public List<IssueLabel> getLabelsForRepo(long repositoryId) {
        return repoIssueDAO.getLabelsByRepo(repositoryId);
    }

    private RepoIssue.Status validateStatus(String status) {
        switch(status.toUpperCase()) {
            case "OPEN", "CLOSED" -> {}
            default -> throw new IllegalArgumentException("Invalid status: " + status);
        }
        return RepoIssue.Status.valueOf(status.toUpperCase());
    }

    private RepoIssue.Priority validatePriority(String priority) {
        switch(priority.toUpperCase()) {
            case "LOW", "MEDIUM", "HIGH" -> {}
            default -> throw new IllegalArgumentException("Invalid priority: " + priority);
        }
        return RepoIssue.Priority.valueOf(priority.toUpperCase());
    }
}
