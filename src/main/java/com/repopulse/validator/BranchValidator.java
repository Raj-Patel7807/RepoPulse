package com.repopulse.validator;

public class BranchValidator {

    private BranchValidator() {

    }

    public static void validateBranchName(String branchName) {
        if(branchName == null || branchName.isBlank() || branchName.length() > 100) {
            throw new IllegalArgumentException("Branch name must be 1-100 characters long");
        }
    }

    public static void validateRepoId(long repoId) {
        if(repoId <= 0) {
            throw new IllegalArgumentException("Invalid repository ID");
        }
    }
}