package com.repopulse.branch.cli;

import com.repopulse.branch.service.BranchService;
import com.repopulse.infra.session.Authz;
import com.repopulse.infra.util.CliUtils;
import com.repopulse.infra.exception.AppException;
import com.repopulse.branch.model.Branch;
import com.repopulse.infra.session.Session;
import com.repopulse.repository.service.RepositoryService;

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
            if(loggedIn) {
                System.out.println("2. Create Branch");
                System.out.println("3. Delete Branch");
                System.out.println("4. Back");
            } else {
                System.out.println("2. Back");
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
            } else if(loggedIn && choice == 2) {
                Authz.requireLogin("create branch");

                if(!repositoryService.canWriteRepository(repoId, Session.getCurrentUser().getUserId())) {
                    throw new AppException("You do not have write access to this repository.");
                }

                String branchName = CliUtils.getStringInput("Enter Branch Name: ");
                int headCommit = CliUtils.getIntInput("Enter Head Commit Id: ");

                branchService.createBranch(repoId, branchName, headCommit);
            } else if(loggedIn && choice == 3) {
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

            } else if((loggedIn && choice == 4) || (!loggedIn && choice == 2)) {
                return;
            } else {
                System.out.println("Invalid Choice...!!!");
            }
        }
    }
}
