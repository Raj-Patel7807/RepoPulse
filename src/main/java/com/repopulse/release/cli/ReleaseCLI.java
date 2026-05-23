package com.repopulse.release.cli;

import com.repopulse.infra.exception.AppException;
import com.repopulse.infra.session.Authz;
import com.repopulse.infra.session.Session;
import com.repopulse.infra.util.CliUtils;
import com.repopulse.release.model.RepoRelease;
import com.repopulse.release.service.RepoReleaseService;
import com.repopulse.repository.service.RepositoryService;

import java.util.List;

public class ReleaseCLI {
    private final long repoId;

    private final RepoReleaseService releaseService = new RepoReleaseService();
    private final RepositoryService repositoryService = new RepositoryService();

    public ReleaseCLI(long repoId) {
        this.repoId = repoId;
    }

    public void start() {
        while(true) {
            boolean loggedIn = Authz.isLoggedIn();
            System.out.println("\n=== Releases ===");
            System.out.println("1. List Releases");
            if(loggedIn) {
                System.out.println("2. Create Release");
                System.out.println("3. Back");
            } else {
                System.out.println("2. Back");
            }

            int choice = CliUtils.getIntInput("Enter Choice: ");

            if(choice == 1) {
                List<RepoRelease> releases = releaseService.getReleasesByRepo(repoId);

                if(releases.isEmpty()) {
                    System.out.println("No Releases Found..!!");
                } else {
                    System.out.println("Releases: ");
                    for(RepoRelease release : releases) {
                        System.out.println(release.getReleaseId() + " | " + release.getTagId() + " | " + release.getReleaseTitle() + " | " + release.getReleaseNotes());
                    }
                }
            } else if(loggedIn && choice == 2) {
                Authz.requireLogin("create release");
                ensureWriteAccess();
                long tagId = CliUtils.getLongInput("Enter Tag Id: ");
                String title = CliUtils.getStringInput("Enter Title: ");
                String description = CliUtils.getStringInput("Enter Description: ");

                releaseService.createRelease(repoId, tagId, title, description, Session.getCurrentUser().getUserId());
            } else if((loggedIn && choice == 3) || (!loggedIn && choice == 2)) {
                return;
            } else {
                System.out.println("Invalid Choice..!!");
            }
        }
    }

    private void ensureWriteAccess() {
        if(Session.getCurrentUser() == null) {
            throw new AppException("Please login first.");
        }
        if(!repositoryService.canWriteRepository(repoId, Session.getCurrentUser().getUserId())) {
            throw new AppException("You do not have write access to this repository.");
        }
    }
}
