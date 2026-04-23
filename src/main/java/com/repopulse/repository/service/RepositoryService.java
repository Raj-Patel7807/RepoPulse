package com.repopulse.repository.service;

import com.repopulse.infra.session.Session;
import com.repopulse.repository.model.*;
import com.repopulse.repository.dao.RepositoryDAO;
import com.repopulse.repository.validator.RepositoryValidator;
import com.repopulse.user.dao.UserDAO;

import java.sql.Timestamp;
import java.util.List;

public class RepositoryService {

    private final RepositoryDAO repositoryDAO;
    private final UserDAO userDAO;

    public RepositoryService() {
        this.repositoryDAO = new RepositoryDAO();
        this.userDAO = new UserDAO();
    }

    public Repository createRepository(String name, String description, long ownerUserId, String visibilityType, Long parentRepoId, Long forkedFromCommitId) {
        RepositoryValidator.validateRepoName(name);
        RepositoryValidator.validateVisibilityType(visibilityType);

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
//        validateRepoName(name);

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
        List<Repository> repos = repositoryDAO.getRepositoriesByUser(userId);
        Long currentUserId = Session.getCurrentUser() == null ? null : Session.getCurrentUser().getUserId();
        if(currentUserId == null || currentUserId == userId || Session.isAdminSession()) {
            return repos;
        }
        repos.removeIf(repo -> userDAO.hasBlockRelationship(currentUserId, repo.getRepoOwnerUserId()));
        return repos;
    }

    public List<Repository> getAllPublicRepositories() {
        List<Repository> repos = repositoryDAO.getAllRepositories("PUBLIC");
        Long currentUserId = Session.getCurrentUser() == null ? null : Session.getCurrentUser().getUserId();
        if(currentUserId == null || Session.isAdminSession()) {
            return repos;
        }
        repos.removeIf(repo -> userDAO.hasBlockRelationship(currentUserId, repo.getRepoOwnerUserId()));
        return repos;
    }

    public List<Repository> getAllPrivateRepositories() {
        return repositoryDAO.getAllRepositories("PRIVATE");
    }

    public void updateRepository(long repoId, String name, String description, String visibilityType, Long defaultBranchId) {
        RepositoryValidator.validateRepoName(name);
        RepositoryValidator.validateVisibilityType(visibilityType);

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
        RepositoryValidator.validateRole(accessRole);

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
        RepositoryValidator.validateRole(accessRole);
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

    public List<Repository> getStarredRepos(long userId) {
        List<Repository> repos = repositoryDAO.getStarredRepos(userId);
        return repos;
    }

    public void unstarRepository(long userId, long repositoryId) {
        repositoryDAO.removeStar(userId, repositoryId);
    }

    public int getRepositoryStars(long repositoryId) {
        return repositoryDAO.getStarCountByRepository(repositoryId);
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
        watcher.setWatchLevel(RepositoryValidator.validateWatchLevel(watchLevelStr));
        watcher.setWatchedAt(new Timestamp(System.currentTimeMillis()));

        repositoryDAO.addOrUpdateWatcher(watcher);
    }

    public List<RepoWatcher> getRepositoryWatchers(long repositoryId) {
        return repositoryDAO.getWatchers(repositoryId);
    }

    public boolean canReadRepository(long repositoryId, Long userId) {
        Repository repo = repositoryDAO.getRepositoryById(repositoryId);
        if(repo == null) return false;
        if(userId != null && !Session.isAdminSession() && userDAO.hasBlockRelationship(userId, repo.getRepoOwnerUserId())) {
            return false;
        }

        String visibility = repo.getRepoVisibilityType();
        if("PUBLIC".equalsIgnoreCase(visibility)) {
            return true;
        }
        if("INTERNAL".equalsIgnoreCase(visibility)) {
            return userId != null;
        }

        if(userId == null) {
            return false;
        }

        if(repo.getRepoOwnerUserId() == userId) {
            return true;
        }
        return repositoryDAO.getCollaboratorRole(repositoryId, userId) != null;
    }

    public boolean canWriteRepository(long repositoryId, Long userId) {
        if(userId == null) {
            return false;
        }

        Repository repo = repositoryDAO.getRepositoryById(repositoryId);
        if(repo == null) return false;
        if(!Session.isAdminSession() && userDAO.hasBlockRelationship(userId, repo.getRepoOwnerUserId())) {
            return false;
        }

        if(repo.getRepoOwnerUserId() == userId) {
            return true;
        }

        String role = repositoryDAO.getCollaboratorRole(repositoryId, userId);
        return "OWNER".equalsIgnoreCase(role) || "MAINTAINER".equalsIgnoreCase(role) || "WRITE".equalsIgnoreCase(role);
    }

    public boolean canManageCollaborators(long repositoryId, Long userId) {
        if(userId == null) {
            return false;
        }

        Repository repo = repositoryDAO.getRepositoryById(repositoryId);
        if(repo == null) return false;
        if(!Session.isAdminSession() && userDAO.hasBlockRelationship(userId, repo.getRepoOwnerUserId())) {
            return false;
        }

        if(repo.getRepoOwnerUserId() == userId) {
            return true;
        }

        String role = repositoryDAO.getCollaboratorRole(repositoryId, userId);
        return "OWNER".equalsIgnoreCase(role) || "MAINTAINER".equalsIgnoreCase(role);
    }

}
