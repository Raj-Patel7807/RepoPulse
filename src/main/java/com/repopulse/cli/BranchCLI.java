package com.repopulse.cli;

import com.repopulse.model.Branch;
import com.repopulse.service.BranchService;

import java.util.List;

public class BranchCLI {

    private final BranchService branchService;
    private final long repoId;

    public BranchCLI(long repoId) {
        this.branchService = new BranchService();
        this.repoId = repoId;
    }

    public void showMenu() {
        while(true) {
            MenuUtils.clearScreen();

            System.out.println("\n=== Branch Menu ===");
            System.out.println("1. List Branches");
            System.out.println("2. Create Branch");
            System.out.println("3. Back");

            int choice = MenuUtils.getIntInput("Enter choice: ");

            if(choice == 1) {
                listBranches();
            } else if(choice == 2) {
                createBranch();
            } else if(choice == 3) {
                return;
            } else {
                System.out.println("Invalid choice!");
                MenuUtils.waitForEnter();
            }
        }
    }

    private void listBranches() {
        try {
            List<Branch> branches = branchService.getBranchesByRepo(repoId);

            if(branches.isEmpty()) {
                System.out.println("No branches found in this repository.");
            } else {
                System.out.println("Branches:");
                for(Branch b : branches) {
                    System.out.println(b.getBranchId() + " | " + b.getBranchName() + " | Head Commit ID: " + b.getHeadCommitId());
                }
            }
        } catch(Exception e) {
            System.out.println("Error fetching branches: " + e.getMessage());
        }

        MenuUtils.waitForEnter();
    }

    private void createBranch() {
        try {
            String branchName = MenuUtils.getStringInput("New Branch Name: ");
            long headCommitId = MenuUtils.getIntInput("Head Commit ID: ");
            Branch branch = branchService.createBranch(repoId, branchName, headCommitId);

            System.out.println("Branch created: " + branch.getBranchName() + " (ID: " + branch.getBranchId() + ")");
        } catch(Exception e) {
            System.out.println("Error creating branch: " + e.getMessage());
        }

        MenuUtils.waitForEnter();
    }
}
