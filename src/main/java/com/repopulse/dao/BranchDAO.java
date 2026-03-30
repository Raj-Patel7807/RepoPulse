package com.repopulse.dao;

import com.repopulse.model.Branch;
import com.repopulse.database.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BranchDAO {

    private final Connection conn;

    public BranchDAO() {
        this.conn = DBConnection.getConnection();
    }

    public long createBranch(Branch branch) {
        String sql = "INSERT INTO branches (repository_id, branch_name, head_commit_id) VALUES (?, ?, ?)";

        try(PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, branch.getRepoId());
            ps.setString(2, branch.getBranchName());
            ps.setLong(3, branch.getHeadCommitId());

            int affectedRows = ps.executeUpdate();
            if(affectedRows == 0) {
                throw new SQLException("Creating branch failed, no rows affected.");
            }

            try(ResultSet rs = ps.getGeneratedKeys()) {
                if(rs.next()) {
                    return rs.getLong(1);
                } else {
                    throw new SQLException("Creating branch failed, no ID obtained.");
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error creating branch", e);
        }
    }

    public Branch getBranchById(long branchId) {
        String sql = "SELECT * FROM branches WHERE branch_id = ?";

        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, branchId);

            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    return mapResultSetToBranch(rs);
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error: ", e);
        }
        return null;
    }

    public List<Branch> getBranchesByRepo(long repoId) {
        List<Branch> branches = new ArrayList<>();

        String sql = "SELECT * FROM branches WHERE repository_id = ?";

        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, repoId);

            try(ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    branches.add(mapResultSetToBranch(rs));
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error: ", e);
        }
        return branches;
    }

    public boolean existsByRepoAndName(long repoId, String branchName) {
        String sql = "SELECT 1 FROM branches WHERE repository_id = ? AND branch_name = ?";

        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, repoId);
            ps.setString(2, branchName);

            try(ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch(SQLException e) {
            throw new RuntimeException("Error: ", e);
        }
    }

    private Branch mapResultSetToBranch(ResultSet rs) throws SQLException {
        Branch branch = new Branch();

        branch.setBranchId(rs.getLong("branch_id"));
        branch.setRepoId(rs.getLong("repository_id"));
        branch.setBranchName(rs.getString("branch_name"));
        branch.setHeadCommitId(rs.getLong("head_commit_id"));

        return branch;
    }
}
