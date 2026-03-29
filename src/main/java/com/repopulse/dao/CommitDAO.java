package com.repopulse.dao;

import com.repopulse.model.Commit;
import com.repopulse.database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.ArrayList;

public class CommitDAOImpl implements CommitDAO {
    @Override
    public void createCommit(Commit commit) {
        String sql = "INSERT INTO commits (repository_id, author_user_id, parent_commit_id, commit_message) VALUES (?, ?, ?, ?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, commit.getRepoId());
            ps.setLong(2, commit.getAuthorUserId());

            if(commit.getParentCommitId() == null) {
                ps.setNull(3, Types.BIGINT);
            } else {
                ps.setLong(3, commit.getParentCommitId());
            }

            ps.setString(4, commit.getCommitMessage());

            ps.executeUpdate();
            System.out.println("Commit added!");

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Commit> getCommitsByRepo(long repoId) {
        List<Commit> commits = new ArrayList<>();
        String sql = "SELECT * FROM commits WHERE repository_id = ? ORDER BY committed_at DESC";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, repoId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Commit c = new Commit();
                c.setCommitId(rs.getLong("commit_id"));
                c.setRepoId(rs.getLong("repository_id"));
                c.setAuthorUserId(rs.getLong("author_user_id"));
                c.setParentCommitId((Long) rs.getObject("parent_commit_id"));
                c.setCommitMessage(rs.getString("commit_message"));

                commits.add(c);
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return commits;
    }
}
