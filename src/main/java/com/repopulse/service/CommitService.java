package com.repopulse.service;

import com.repopulse.model.Commit;

import java.util.List;

public interface CommitService {
    void addCommit(String msg, long repoId, long userId);

    List<Commit> getHistory(long repoId);
}
