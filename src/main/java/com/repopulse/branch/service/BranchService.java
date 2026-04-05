package com.repopulse.branch.service;

import com.repopulse.branch.model.Branch;
import com.repopulse.branch.dao.BranchDAO;
import com.repopulse.branch.model.BranchMerge;
import com.repopulse.branch.validator.BranchValidator;

import java.sql.SQLException;
import java.util.List;

public class BranchService {

    private final BranchDAO branchDAO;

    public BranchService() {
        this.branchDAO = new BranchDAO();
    }

    public Branch createBranch(long repoId, String branchName, long headCommitId) throws SQLException {
        BranchValidator.validateBranchName(branchName);
        BranchValidator.validateRepoId(repoId);

        if(branchDAO.existsByRepoAndName(repoId, branchName)) {
            throw new IllegalArgumentException("Branch name already exists in this repository");
        }

        Branch branch = new Branch();

        branch.setRepoId(repoId);
        branch.setBranchName(branchName);
        branch.setHeadCommitId(headCommitId);

        long branchId = branchDAO.createBranch(branch);
        branch.setBranchId(branchId);

        return branch;
    }

    public List<Branch> getBranchesByRepo(long repoId) throws SQLException {
        return branchDAO.getBranchesByRepo(repoId);
    }

    public Branch getBranchById(long branchId) throws SQLException {
        return branchDAO.getBranchById(branchId);
    }

    public boolean createMerge(BranchMerge merge) {
        return branchDAO.addBranchMerge(merge);
    }

    public BranchMerge getMergeById(long mergeId) {
        return branchDAO.getBranchMergeById(mergeId);
    }

    public List<BranchMerge> getAllMerges() {
        return branchDAO.getAllBranchMerges();
    }

    public boolean updateMerge(BranchMerge merge) {
        return branchDAO.updateBranchMerge(merge);
    }

    public boolean deleteMerge(long mergeId) {
        return branchDAO.deleteBranchMerge(mergeId);
    }
}
