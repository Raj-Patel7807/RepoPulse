package com.repopulse.repository.service;

import com.repopulse.infra.session.Session;
import com.repopulse.repository.model.*;
import com.repopulse.repository.dao.RepositoryDAO;

import java.sql.Timestamp;
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

    public void cloneRepo(long repoId, String cloneType) {
        long userId = Session.getCurrentUser().getUserId();

        RepoClone clone = new RepoClone();
        clone.setRepositoryId(repoId);
        clone.setClonedByUserId(userId);
        clone.setCloneType(cloneType);

        repositoryDAO.createClone(clone);
    }

    public List<RepoClone> getClonesByRepo(long repoId) {
        List<RepoClone> clones = repositoryDAO.getClonesByRepoId(repoId);
        return clones;
    }

    public List<RepoClone> getClonesByUser(long userId) {
        List<RepoClone> clones = repositoryDAO.getClonesByUserId(userId);
        return clones;
    }

    public void addCollaborator(long repoId, long userId, String accessRole) {
        validateRole(accessRole);

        RepoCollaborator collaborator = new RepoCollaborator();
        collaborator.setRepositoryId(repoId);
        collaborator.setUserId(userId);
        collaborator.setAccessRole(accessRole);

        repositoryDAO.addCollaborator(collaborator);
    }

    public void removeCollaborator(long repoId, long userId) {
        repositoryDAO.removeCollaborator(repoId, userId);
    }

    public void updateAccessRole(long repoId, long userId, String accessRole) {
        validateRole(accessRole);
        repositoryDAO.updateAccessRole(repoId, userId, accessRole);
    }

    public List<RepoCollaborator> getCollaboratorsForRepo(long repoId) {
        return repositoryDAO.getCollaboratorsByRepo(repoId);
    }

    public List<RepoCollaborator> getCollaborationsByUser(long userId) {
        return repositoryDAO.getCollaboratorsByUser(userId);
    }

    public void starRepository(long userId, long repositoryId) {
        RepoStar star = new RepoStar();
        star.setUserId(userId);
        star.setRepositoryId(repositoryId);
        star.setStarredAt(new Timestamp(System.currentTimeMillis()));

        repositoryDAO.addStar(star);
    }

    public void unstarRepository(long userId, long repositoryId) {
        repositoryDAO.removeStar(userId, repositoryId);
    }

    public List<RepoStar> getRepositoryStars(long repositoryId) {
        return repositoryDAO.getStarsByRepository(repositoryId);
    }

    public RepoTag createTag(long repositoryId, long commitId, String tagName, String tagDescription) {
        RepoTag tag = new RepoTag();

        tag.setRepositoryId(repositoryId);
        tag.setCommitId(commitId);
        tag.setTagName(tagName);
        tag.setTagDescription(tagDescription);

        repositoryDAO.createTag(tag);

        return tag;
    }

    public RepoTag getTagById(long tagId) {
        return repositoryDAO.getTagById(tagId);
    }

    public List<RepoTag> getTagsByRepositoryId(long repositoryId) {
        return repositoryDAO.getTagsByRepositoryId(repositoryId);
    }

    public void watchRepository(long userId, long repositoryId, String watchLevelStr) {
        RepoWatcher watcher = new RepoWatcher();
        watcher.setUserId(userId);
        watcher.setRepositoryId(repositoryId);
        watcher.setWatchLevel(validateWatchLevel(watchLevelStr));
        watcher.setWatchedAt(new Timestamp(System.currentTimeMillis()));

        repositoryDAO.addOrUpdateWatcher(watcher);
    }

    public List<RepoWatcher> getRepositoryWatchers(long repositoryId) {
        return repositoryDAO.getWatchers(repositoryId);
    }

    private RepoWatcher.WatchLevel validateWatchLevel(String level) {
        switch(level.toUpperCase()) {
            case "ALL", "PARTICIPATING", "NONE" -> {}
            default -> throw new IllegalArgumentException("Invalid watch level: " + level);
        }
        return RepoWatcher.WatchLevel.valueOf(level.toUpperCase());
    }

    private void validateRole(String role) {
        switch(role.toUpperCase()) {
            case "OWNER", "MAINTAINER", "WRITE", "READ" -> {}
            default -> throw new IllegalArgumentException("Invalid access role: " + role);
        }
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
