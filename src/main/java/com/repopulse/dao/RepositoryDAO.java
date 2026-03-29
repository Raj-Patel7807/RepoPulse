package com.repopulse.dao;

import com.repopulse.model.Repository;
import com.repopulse.database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepositoryDAOImpl implements RepositoryDAO {
    @Override
    public void createRepository(Repository repo) {
        String sql = "INSERT INTO repositories (repository_name, repository_description, owner_user_id, visibility_type) VALUES (?, ?, ?, ?)";

        try(Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, repo.getRepoName());
            ps.setString(2, repo.getRepoDesc());
            ps.setLong(3, repo.getRepoOwnerUserId());
            ps.setString(4, repo.getRepoVisibilityType());

            ps.executeUpdate();
            System.out.println("Repository created successfully!");
        } catch(SQLException e) {
            if(e.getMessage().contains("unique")) {
                System.out.println("Repository name already exists!");
            } else {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Repository> getRepositoriesByUser(long userId) {
        List<Repository> repos = new ArrayList<>();
        String sql = "SELECT * FROM repositories WHERE owner_user_id = ? AND is_deleted = FALSE";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Repository repo = new Repository();
                repo.setRepoId(rs.getLong("repository_id"));
                repo.setRepoName(rs.getString("repository_name"));
                repo.setRepoDesc(rs.getString("repository_description"));
                repo.setRepoOwnerUserId(rs.getLong("owner_user_id"));
                repo.setRepoVisibilityType(rs.getString("visibility_type"));

                repos.add(repo);
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return repos;
    }

    @Override
    public Repository getRepositoryById(long repoId) {
        String sql = "SELECT * FROM repositories WHERE repository_id = ?";
        Repository repo = null;

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, repoId);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                repo = new Repository();
                repo.setRepoId(rs.getLong("repository_id"));
                repo.setRepoName(rs.getString("repository_name"));
                repo.setRepoDesc(rs.getString("repository_description"));
                repo.setRepoOwnerUserId(rs.getLong("owner_user_id"));
                repo.setRepoVisibilityType(rs.getString("visibility_type"));
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return repo;
    }
}
