package com.repopulse.service;

import com.repopulse.model.Repository;

import java.util.List;

public interface RepositoryService {
    void createRepo(String name, String desc, long userId);

    List<Repository> getUserRepos(long userId);
}
