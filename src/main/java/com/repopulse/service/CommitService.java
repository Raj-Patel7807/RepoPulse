package com.repopulse.service;

import com.repopulse.model.Commit;

import java.util.List;

public class CommitServiceImpl implements CommitService {
    private CommitDAO commitDAO;

    public CommitServiceImpl(CommitDAO commitDAO) {
        this.commitDAO = commitDAO;
    }

    @Override
    public void addCommit(String msg, long repoId, long userId) {
        Commit commit = new Commit();
        commit.setRepoId(repoId);
        commit.setAuthorUserId(userId);
        commit.setCommitMessage(msg);

        commitDAO.createCommit(commit);
    }

    @Override
    public List<Commit> getHistory(long repoId) {
        return commitDAO.getCommitsByRepo(repoId);
    }
}
