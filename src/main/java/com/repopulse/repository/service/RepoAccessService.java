package com.repopulse.repository.service;

import com.repopulse.infra.exception.AppException;
import com.repopulse.infra.session.Session;
import com.repopulse.repository.dao.RepositoryDAO;
import com.repopulse.repository.model.Repository;

public class RepoAccessService {
    private final RepositoryDAO repositoryDAO = new RepositoryDAO();

    public boolean canReadRepository(Long userId, long repoId) {
        Repository repo = repositoryDAO.getRepositoryById(repoId);
        if(repo == null) {
            return false;
        }

        String visibility = repo.getRepoVisibilityType();
        if("PUBLIC".equalsIgnoreCase(visibility)) {
            return true;
        }
        if("INTERNAL".equalsIgnoreCase(visibility)) {
            return userId != null;
        }
        return isOwnerOrCollaborator(userId, repoId, false);
    }

    public boolean canWriteRepository(Long userId, long repoId) {
        return isOwnerOrCollaborator(userId, repoId, true);
    }

    public void requireReadAccess(long repoId) {
        Long userId = Session.getCurrentUser() == null ? null : Session.getCurrentUser().getUserId();
        if(!canReadRepository(userId, repoId)) {
            throw new AppException("You do not have read access to this repository.");
        }
    }

    public void requireWriteAccess(long repoId) {
        Long userId = Session.getCurrentUser() == null ? null : Session.getCurrentUser().getUserId();
        if(!canWriteRepository(userId, repoId)) {
            throw new AppException("You do not have write access to this repository.");
        }
    }

    private boolean isOwnerOrCollaborator(Long userId, long repoId, boolean writeRequired) {
        if(userId == null) {
            return false;
        }

        Repository repo = repositoryDAO.getRepositoryById(repoId);
        if(repo == null) {
            return false;
        }

        if(repo.getRepoOwnerUserId() == userId) {
            return true;
        }

        String role = repositoryDAO.getCollaboratorRole(repoId, userId);
        if(role == null) {
            return false;
        }

        if(!writeRequired) {
            return true;
        }

        return "OWNER".equalsIgnoreCase(role) || "MAINTAINER".equalsIgnoreCase(role) || "WRITE".equalsIgnoreCase(role);
    }
}

