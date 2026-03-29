package com.repopulse.dao;

import com.repopulse.model.Commit;

import java.util.List;

public interface CommitDAO {
    void createCommit(Commit commit);

    List<Commit> getCommitsByRepo(long repoId);
}
