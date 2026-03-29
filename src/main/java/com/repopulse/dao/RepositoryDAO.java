package com.repopulse.dao;

import com.repopulse.model.Repository;

import java.util.List;

public interface RepositoryDAO {
    void createRepository(Repository repo);

    List<Repository> getRepositoriesByUser(long userId);

    Repository getRepositoryById(long repoId);
}
