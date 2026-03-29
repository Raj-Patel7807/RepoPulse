package com.repopulse.service;

import com.repopulse.model.Repository;

import java.util.List;

public class RepositoryServiceImpl implements RepositoryService {
    private RepositoryDAO repoDAO;

    public RepositoryServiceImpl(RepositoryDAO repoDAO) {
        this.repoDAO = repoDAO;
    }

    @Override
    public void createRepo(String name, String desc, long userId) {
        Repository repo = new Repository();
        repo.setRepoName(name);
        repo.setRepoDesc(desc);
        repo.setRepoOwnerUserId(userId);
        repo.setRepoVisibilityType("PUBLIC");

        repoDAO.createRepository(repo);
    }

    @Override
    public List<Repository> getUserRepos(long userId) {
        return repoDAO.getRepositoriesByUser(userId);
    }
}
