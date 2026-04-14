package com.repopulse.branch.cli;

import com.repopulse.branch.service.BranchService;
import com.repopulse.common.cli.CliUtils;
import com.repopulse.branch.model.Branch;

import java.util.List;

public class BranchCLI {
    private final long repoId;

    private final BranchService branchService = new BranchService();

    public BranchCLI(long repoId) {
        this.repoId = repoId;
    }

    public void start() {
        while(true) {
            System.out.println("\n=== Branches ===");
            System.out.println("1. List Branches");
            System.out.println("2. Create Branch");
            System.out.println("3. Delete Branch");
            System.out.println("4. Back");

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
                String branchName = CliUtils.getStringInput("Enter Branch Name: ");
                int headCommit = CliUtils.getIntInput("Enter Head Commit Id: ");

                branchService.createBranch(repoId, branchName, headCommit);
            } else if(choice == 3) {
                String branchName = CliUtils.getStringInput("Enter BranchId to delete: ");

                System.out.println("Pending Branch Deletion.... Try Again Later...");
            } else if(choice == 4) {
                return;
            } else {
                System.out.println("Invalid Choice...!!!");
            }
        }
    }
}
