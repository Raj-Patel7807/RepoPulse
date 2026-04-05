package com.repopulse.branch.dao;

import com.repopulse.branch.model.Branch;
import com.repopulse.branch.model.BranchMerge;
import com.repopulse.infra.database.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
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

    public boolean addBranchMerge(BranchMerge merge) {
        String sql = "INSERT INTO branch_merges (repository_id, source_branch_id, target_branch_id, merge_commit_id, merged_by_user_id, merge_strategy, merged_at) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, merge.getRepositoryId());
            stmt.setLong(2, merge.getSourceBranchId());
            stmt.setLong(3, merge.getTargetBranchId());
            stmt.setLong(4, merge.getMergeCommitId());
            stmt.setLong(5, merge.getMergedByUserId());
            stmt.setString(6, merge.getMergeStrategy().name());
            stmt.setTimestamp(7, merge.getMergedAt());

            return stmt.executeUpdate() > 0;
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public BranchMerge getBranchMergeById(long mergeId) {
        String sql = "SELECT * FROM branch_merges WHERE merge_id = ?";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, mergeId);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                return mapResultSetToBranchMerge(rs);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<BranchMerge> getAllBranchMerges() {
        List<BranchMerge> merges = new ArrayList<>();

        String sql = "SELECT * FROM branch_merges";

        try(Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                merges.add(mapResultSetToBranchMerge(rs));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return merges;
    }

    public boolean updateBranchMerge(BranchMerge merge) {
        String sql = """
                      UPDATE branch_merges SET repository_id=?, source_branch_id=?, target_branch_id=?, merge_commit_id=?, merged_by_user_id=?, merge_strategy=?, merged_at=?
                      WHERE merge_id=?
                      """;

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, merge.getRepositoryId());
            stmt.setLong(2, merge.getSourceBranchId());
            stmt.setLong(3, merge.getTargetBranchId());
            stmt.setLong(4, merge.getMergeCommitId());
            stmt.setLong(5, merge.getMergedByUserId());
            stmt.setString(6, merge.getMergeStrategy().name());
            stmt.setTimestamp(7, merge.getMergedAt());
            stmt.setLong(8, merge.getMergeId());

            return stmt.executeUpdate() > 0;
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteBranchMerge(long mergeId) {
        String sql = "DELETE FROM branch_merges WHERE merge_id=?";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, mergeId);

            return stmt.executeUpdate() > 0;
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private BranchMerge mapResultSetToBranchMerge(ResultSet rs) throws SQLException {
        BranchMerge merge = new BranchMerge();

        merge.setMergeId(rs.getLong("merge_id"));
        merge.setRepositoryId(rs.getLong("repository_id"));
        merge.setSourceBranchId(rs.getLong("source_branch_id"));
        merge.setTargetBranchId(rs.getLong("target_branch_id"));
        merge.setMergeCommitId(rs.getLong("merge_commit_id"));
        merge.setMergedByUserId(rs.getLong("merged_by_user_id"));
        merge.setMergeStrategy(BranchMerge.MergeStrategy.valueOf(rs.getString("merge_strategy")));
        merge.setMergedAt(rs.getTimestamp("merged_at"));

        return merge;
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
