package com.repopulse.branch.cli;

import com.repopulse.branch.model.Branch;
import com.repopulse.branch.model.BranchMerge;
import com.repopulse.branch.service.BranchService;
import com.repopulse.infra.exception.AppException;
import com.repopulse.infra.session.Authz;
import com.repopulse.infra.session.Session;
import com.repopulse.infra.util.CliUtils;
import com.repopulse.repository.service.RepositoryService;

import java.sql.Timestamp;
import java.util.List;

public class BranchCLI {
    private final long repoId;

    private final BranchService branchService = new BranchService();
    private final RepositoryService repositoryService = new RepositoryService();

    public BranchCLI(long repoId) {
        this.repoId = repoId;
    }

    public void start() {
        while(true) {
            boolean loggedIn = Authz.isLoggedIn();
            System.out.println("\n=== Branches ===");
            System.out.println("1. List Branches");
            System.out.println("2. List Merges");
            if(loggedIn) {
                System.out.println("3. Create Branch");
                System.out.println("4. Delete Branch");
                System.out.println("5. Create Merge Record");
                System.out.println("6. Delete Merge Record");
                System.out.println("7. Back");
            } else {
                System.out.println("3. Back");
            }

            int choice = CliUtils.getIntInput("Enter Choice: ");

            if(choice == 1) {
                List<Branch> branches = branchService.getBranchesByRepo(repoId);

                if(branches.isEmpty()) {
                    System.out.println("No Branches Found!!");
                } else {
                    System.out.println("Branches: ");
                    for(Branch branch : branches) {
                        System.out.println(branch.getBranchId() + " | " + branch.getBranchName());
                    }

                    CliUtils.waitForEnter();
                }
            } else if(choice == 2) {
                List<BranchMerge> merges = branchService.getAllMerges();
                if(merges.isEmpty()) {
                    System.out.println("No merges found.");
                } else {
                    System.out.println("merge_id | source_branch | target_branch | strategy");
                    for(BranchMerge merge : merges) {
                        if(merge.getRepositoryId() == repoId) {
                            System.out.println(merge.getMergeId() + " | " + merge.getSourceBranchId() + " | " + merge.getTargetBranchId() + " | " + merge.getMergeStrategy());
                        }
                    }
                }
                CliUtils.waitForEnter();
            } else if(loggedIn && choice == 3) {
                Authz.requireLogin("create branch");

                if(!repositoryService.canWriteRepository(repoId, Session.getCurrentUser().getUserId())) {
                    throw new AppException("You do not have write access to this repository.");
                }

                String branchName = CliUtils.getStringInput("Enter Branch Name: ");
                int headCommit = CliUtils.getIntInput("Enter Head Commit Id: ");

                branchService.createBranch(repoId, branchName, headCommit);
            } else if(loggedIn && choice == 4) {
                Authz.requireLogin("delete branch");

                if(!repositoryService.canWriteRepository(repoId, Session.getCurrentUser().getUserId())) {
                    throw new AppException("You do not have write access to this repository.");
                }

                long branchId = CliUtils.getLongInput("Enter Branch ID to delete: ");
                boolean deleted = branchService.deleteBranch(repoId, branchId);

                if(deleted) {
                    System.out.println("Branch deleted.");
                } else {
                    System.out.println("Branch not found.");
                }
                CliUtils.waitForEnter();

            } else if(loggedIn && choice == 5) {
                Authz.requireLogin("create merge record");
                if(!repositoryService.canWriteRepository(repoId, Session.getCurrentUser().getUserId())) {
                    throw new AppException("You do not have write access to this repository.");
                }
                BranchMerge merge = new BranchMerge();
                merge.setRepositoryId(repoId);
                merge.setSourceBranchId(CliUtils.getLongInput("Source Branch ID: "));
                merge.setTargetBranchId(CliUtils.getLongInput("Target Branch ID: "));
                merge.setMergeCommitId(CliUtils.getLongInput("Merge Commit ID: "));
                merge.setMergedByUserId(Session.getCurrentUser().getUserId());
                String strategy = CliUtils.getStringInput("Strategy (MERGE/SQUASH/REBASE): ");
                merge.setMergeStrategy(BranchMerge.MergeStrategy.valueOf(strategy.toUpperCase()));
                merge.setMergedAt(new Timestamp(System.currentTimeMillis()));
                boolean created = branchService.createMerge(merge);
                System.out.println(created ? "Merge record created." : "Failed to create merge record.");
                CliUtils.waitForEnter();
            } else if(loggedIn && choice == 6) {
                Authz.requireLogin("delete merge record");
                if(!repositoryService.canWriteRepository(repoId, Session.getCurrentUser().getUserId())) {
                    throw new AppException("You do not have write access to this repository.");
                }
                long mergeId = CliUtils.getLongInput("Merge ID to delete: ");
                boolean deleted = branchService.deleteMerge(mergeId);
                System.out.println(deleted ? "Merge record deleted." : "Merge record not found.");
                CliUtils.waitForEnter();
            } else if((loggedIn && choice == 7) || (!loggedIn && choice == 3)) {
                return;
            } else {
                System.out.println("Invalid Choice...!!!");
            }
        }
    }
}
