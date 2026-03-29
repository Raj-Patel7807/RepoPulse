package com.repopulse.service;

import com.repopulse.dao.RepositoryDAO;
import com.repopulse.model.Repository;

import java.util.List;

public class RepositoryService {

    private final RepositoryDAO repositoryDAO;

    public RepositoryService() {
        this.repositoryDAO = new RepositoryDAO();
    }

    public Repository createRepository(String name, String description, long ownerUserId, String visibilityType, Long parentRepoId, Long forkedFromCommitId) {
        validateRepoName(name);
        validateVisibilityType(visibilityType);

        Repository repo = new Repository();

        repo.setRepoName(name);
        repo.setRepoDesc(description);
        repo.setRepoOwnerUserId(ownerUserId);
        repo.setRepoVisibilityType(visibilityType);
        repo.setParentRepositoryId(parentRepoId);
        repo.setForkedFromCommitId(forkedFromCommitId);

        repositoryDAO.createRepository(repo);
        return repo;
    }

    public Repository createRepository(String name, long ownerUserId, boolean isPublic) {
        validateRepoName(name);

        Repository repo = new Repository();
        repo.setRepoName(name);
        repo.setRepoOwnerUserId(ownerUserId);

        if(isPublic) {
            repo.setRepoVisibilityType("PUBLIC");
        } else {
            repo.setRepoVisibilityType("PRIVATE");
        }

        repositoryDAO.createRepository(repo);
        return repo;
    }

    public Repository getRepositoryById(long repoId) {
        return repositoryDAO.getRepositoryById(repoId);
    }

    public List<Repository> getRepositoriesByUser(long userId) {
        return repositoryDAO.getRepositoriesByUser(userId);
    }

    public List<Repository> getAllPublicRepositories() {
        return repositoryDAO.getAllRepositories("PUBLIC");
    }

    public List<Repository> getAllPrivateRepositories() {
        return repositoryDAO.getAllRepositories("PRIVATE");
    }

    public void updateRepository(long repoId, String name, String description, String visibilityType, Long defaultBranchId) {
        validateRepoName(name);
        validateVisibilityType(visibilityType);

        Repository repo = repositoryDAO.getRepositoryById(repoId);
        if(repo == null) {
            throw new IllegalArgumentException("Repository not found");
        }

        repo.setRepoName(name);
        repo.setRepoDesc(description);
        repo.setRepoVisibilityType(visibilityType);
        repo.setDefaultBranchId(defaultBranchId);

        repositoryDAO.updateRepository(repo);
    }

    public void deleteRepository(long repoId, Long deletedByUserId) {
        repositoryDAO.softDeleteRepository(repoId, deletedByUserId);
    }

    private void validateRepoName(String name) {
        if(name == null || name.trim().length() < 1 || name.length() > 100) {
            throw new IllegalArgumentException("Repository name must be 1-100 characters");
        }
    }

    private void validateVisibilityType(String visibilityType) {
        if(!"PUBLIC".equalsIgnoreCase(visibilityType) && !"PRIVATE".equalsIgnoreCase(visibilityType)) {
            throw new IllegalArgumentException("Visibility type must be PUBLIC or PRIVATE");
        }
    }
}
