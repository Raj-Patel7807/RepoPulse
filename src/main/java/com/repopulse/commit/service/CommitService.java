package com.repopulse.commit.service;

import com.repopulse.commit.dao.CommitDAO;
import com.repopulse.commit.model.Commit;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class CommitService {

    private final CommitDAO commitDAO;

    public CommitService() {
        this.commitDAO = new CommitDAO();
    }

    public Commit createCommit(long repoId, long authorUserId, Long parentCommitId, String commitMessage) {
        if(commitMessage == null || commitMessage.isEmpty()) {
            throw new IllegalArgumentException("Commit message cannot be empty");
        }

        Commit commit = new Commit();

        commit.setRepoId(repoId);
        commit.setAuthorUserId(authorUserId);
        commit.setParentCommitId(parentCommitId);
        commit.setCommitMessage(commitMessage);

        String hash = generateCommitHash(repoId, authorUserId, parentCommitId, commitMessage);
        commit.setCommitHash(hash);

        long commitId = commitDAO.createCommit(commit);
        if(commitId == -1) {
            throw new RuntimeException("Failed to create commit");
        }

        commit.setCommitId(commitId);
        return commit;
    }

    public Commit getCommitById(long commitId) {
        return commitDAO.getCommitById(commitId);
    }

    public List<Commit> getCommitsByRepo(long repoId) {
        return commitDAO.getCommitsByRepo(repoId);
    }

    public List<Commit> getCommitsByAuthor(long authorUserId) {
        return commitDAO.getCommitsByAuthor(authorUserId);
    }

    public Commit getLatestCommit(long repoId) {
        return commitDAO.getLatestCommit(repoId);
    }

    public void deleteCommit(long commitId) {
        commitDAO.deleteCommit(commitId);
    }

    private String generateCommitHash(long repoId, long authorUserId, Long parentCommitId, String commitMessage) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            String input = repoId + "|" + authorUserId + "|" + (parentCommitId != null ? parentCommitId : "") + "|" + commitMessage + "|" + System.currentTimeMillis();
            byte[] hashBytes = md.digest(input.getBytes());

            StringBuilder sb = new StringBuilder();

            for(byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch(NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
