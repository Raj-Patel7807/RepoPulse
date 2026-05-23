package com.repopulse.repository.cli;

import com.repopulse.infra.exception.AppException;
import com.repopulse.infra.session.Authz;
import com.repopulse.infra.session.Session;
import com.repopulse.infra.util.CliUtils;
import com.repopulse.repository.model.RepoCollaborator;
import com.repopulse.repository.service.RepositoryService;

import java.util.List;

public class RepoCollaboratorCLI {
    private final long repoId;
    private final RepositoryService repositoryService = new RepositoryService();

    public RepoCollaboratorCLI(long repoId) {
        this.repoId = repoId;
    }

    public void start() {
        if(Session.getCurrentUser() == null || !repositoryService.canManageCollaborators(repoId, Session.getCurrentUser().getUserId())) {
            throw new AppException("You are not allowed to manage collaborators for this repository.");
        }

        while(true) {
            System.out.println("\n=== Collaborators ===");
            System.out.println("1. List Collaborators");
            System.out.println("2. Add Collaborator");
            System.out.println("3. Update Access Role");
            System.out.println("4. Remove Collaborator");
            System.out.println("5. Back");

            int choice = CliUtils.getIntInput("Enter Choice: ");

            if(choice == 1) {
                List<RepoCollaborator> list = repositoryService.getCollaboratorsForRepo(repoId);
                if(list.isEmpty()) {
                    System.out.println("No collaborators found.");
                } else {
                    System.out.println("user_id | role");
                    for(RepoCollaborator c : list) {
                        System.out.println(c.getUserId() + " | " + c.getAccessRole());
                    }
                }
                CliUtils.waitForEnter();
            } else if(choice == 2) {
                Authz.requireLogin("add collaborator");
                long userId = CliUtils.getLongInput("Enter User ID: ");
                String role = CliUtils.getStringInput("Role (OWNER/MAINTAINER/WRITE/READ): ");
                repositoryService.addCollaborator(repoId, userId, role);
                System.out.println("Collaborator added.");
                CliUtils.waitForEnter();
            } else if(choice == 3) {
                Authz.requireLogin("update collaborator role");
                long userId = CliUtils.getLongInput("Enter User ID: ");
                String role = CliUtils.getStringInput("New Role (OWNER/MAINTAINER/WRITE/READ): ");
                repositoryService.updateAccessRole(repoId, userId, role);
                System.out.println("Role updated.");
                CliUtils.waitForEnter();
            } else if(choice == 4) {
                Authz.requireLogin("remove collaborator");
                long userId = CliUtils.getLongInput("Enter User ID: ");
                repositoryService.removeCollaborator(repoId, userId);
                System.out.println("Collaborator removed.");
                CliUtils.waitForEnter();
            } else if(choice == 5) {
                return;
            } else {
                System.out.println("Invalid choice!");
                CliUtils.waitForEnter();
            }
        }
    }
}
