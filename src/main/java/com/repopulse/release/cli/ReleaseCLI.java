package com.repopulse.release.cli;

import com.repopulse.common.cli.CliUtils;
import com.repopulse.infra.session.Session;
import com.repopulse.release.model.RepoRelease;
import com.repopulse.release.service.RepoReleaseService;

import java.util.List;

public class ReleaseCLI {
    private final long repoId;

    private final RepoReleaseService releaseService = new RepoReleaseService();

    public ReleaseCLI(long repoId) {
        this.repoId = repoId;
    }

    public void start() {
        while(true) {
            System.out.println("\n=== Releases ===");
            System.out.println("1. List Releases");
            System.out.println("2. Create Release");
            System.out.println("3. Back");

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
            } else if(choice == 2) {
                long tagId = CliUtils.getLongInput("Enter Tag Id: ");
                String title = CliUtils.getStringInput("Enter Title: ");
                String description = CliUtils.getStringInput("Enter Description: ");

                releaseService.createRelease(repoId, tagId, title, description, Session.getCurrentUser().getUserId());
            } else if(choice == 3) {
                return;
            } else {
                System.out.println("Invalid Choice..!!");
            }
        }
    }
}
