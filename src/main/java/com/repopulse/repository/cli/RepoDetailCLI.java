package com.repopulse.repository.cli;

import com.repopulse.branch.cli.BranchCLI;
import com.repopulse.commit.service.CommitService;
import com.repopulse.infra.session.Authz;
import com.repopulse.commit.cli.CommitCLI;
import com.repopulse.infra.util.CliUtils;
import com.repopulse.infra.exception.AppException;
import com.repopulse.discussion.cli.DiscussionCommentCLI;
import com.repopulse.infra.session.Session;
import com.repopulse.issue.cli.IssueCLI;
import com.repopulse.pullrequest.cli.PullRequestCLI;
import com.repopulse.release.cli.ReleaseCLI;
import com.repopulse.repository.model.RepoTag;
import com.repopulse.repository.model.RepoWatcher;
import com.repopulse.repository.model.Repository;
import com.repopulse.repository.service.RepositoryService;

import java.util.List;

public class RepoDetailCLI {
    private final RepositoryService repositoryService = new RepositoryService();
    private final CommitService commitService = new CommitService();

    private final Repository repo;

    public RepoDetailCLI(long repoId) {
        this.repo = repositoryService.getRepositoryById(repoId);
    }

    public void start() {
        while(true) {
            if(repo == null) {
                System.out.println("Repository not found.");
                CliUtils.waitForEnter();
                return;
            }

            Long currentUserId = Session.getCurrentUser() == null ? null : Session.getCurrentUser().getUserId();
            if(!repositoryService.canReadRepository(repo.getRepoId(), currentUserId)) {
                throw new AppException("You do not have access to this repository.");
            }
            System.out.println("\n=== Repository " + repo.getRepoName() + " ===");
            System.out.println("Repo Id: " + repo.getRepoId());
            System.out.println("Total Starts: " + repositoryService.getRepositoryStars(repo.getRepoId()));
            System.out.println("Total Commits: " + commitService.getCommitsByRepo(repo.getRepoId()).size());
            System.out.println();

            boolean loggedIn = Authz.isLoggedIn();
            System.out.println("1. Code");
            System.out.println("2. Commits");
            System.out.println("3. Branches");
            System.out.println("4. Pull Requests");
            System.out.println("5. Issue");
            System.out.println("6. Releases");
            System.out.println("7. Discussions");
            if(loggedIn) {
                System.out.println("8. Collaborators");
                System.out.println("9. Star Repo");
                System.out.println("10. Unstar Repo");
                System.out.println("11. Clone Repo");
                System.out.println("12. Watch Repo");
                System.out.println("13. Create Tag");
                System.out.println("14. List Tags");
                System.out.println("15. List Watchers");
                System.out.println("16. Back");
            } else {
                System.out.println("8. Back");
            }

            int choice = CliUtils.getIntInput("Enter Choice: ");

            if(choice == 1) {
                RepoFileCLI repoFileCLI = new RepoFileCLI(repo.getRepoId());
                repoFileCLI.start();
            } else if(choice == 2) {
                CommitCLI commitCLI = new CommitCLI(repo.getRepoId());
                commitCLI.start();

            } else if(choice == 3) {
                BranchCLI branchCLI = new BranchCLI(repo.getRepoId());
                branchCLI.start();

            } else if(choice == 4) {
                PullRequestCLI pullRequestCLI = new PullRequestCLI(repo.getRepoId());
                pullRequestCLI.start();

            } else if(choice == 5) {
                IssueCLI issueCLI = new IssueCLI(repo.getRepoId());
                issueCLI.start();

            } else if(choice == 6) {
                ReleaseCLI releaseCLI = new ReleaseCLI(repo.getRepoId());
                releaseCLI.start();

            } else if(choice == 7) {
                DiscussionCommentCLI discussionCommentCLI = new DiscussionCommentCLI(repo.getRepoId());
                discussionCommentCLI.start();
            } else if(choice == 8 && loggedIn) {
                Authz.requireLogin("manage collaborators");
                if(!repositoryService.canManageCollaborators(repo.getRepoId(), Session.getCurrentUser().getUserId())) {
                    throw new AppException("You are not allowed to manage collaborators for this repository.");
                }
                RepoCollaboratorCLI repoCollaboratorCLI = new RepoCollaboratorCLI(repo.getRepoId());
                repoCollaboratorCLI.start();
            } else if(choice == 9 && loggedIn) {
                Authz.requireLogin("star repository");
                repositoryService.starRepository(Session.getCurrentUser().getUserId(), repo.getRepoId());
                System.out.println("Repository Starred!!");
                CliUtils.waitForEnter();

            } else if(choice == 10 && loggedIn) {
                Authz.requireLogin("unstar repository");
                repositoryService.unstarRepository(Session.getCurrentUser().getUserId(), repo.getRepoId());
                System.out.println("Repository Unstarred!!");
                CliUtils.waitForEnter();
            } else if(choice == 11 && loggedIn) {
                Authz.requireLogin("clone repository");
                String cloneType = CliUtils.getStringInput("Clone Type (HTTPS/SSH): ");
                repositoryService.cloneRepo(repo.getRepoId(), cloneType);
                System.out.println("Clone recorded.");
                CliUtils.waitForEnter();
            } else if(choice == 12 && loggedIn) {
                Authz.requireLogin("watch repository");
                String watchLevel = CliUtils.getStringInput("Watch Level (ALL/PARTICIPATING/NONE): ");
                repositoryService.watchRepository(Session.getCurrentUser().getUserId(), repo.getRepoId(), watchLevel);
                System.out.println("Watch preference updated.");
                CliUtils.waitForEnter();
            } else if(choice == 13 && loggedIn) {
                Authz.requireLogin("create tag");
                if(!repositoryService.canWriteRepository(repo.getRepoId(), Session.getCurrentUser().getUserId())) {
                    throw new AppException("You do not have write access to this repository.");
                }
                long commitId = CliUtils.getLongInput("Commit Id: ");
                String tagName = CliUtils.getStringInput("Tag Name: ");
                String tagDescription = CliUtils.getStringInput("Tag Description: ");
                repositoryService.createTag(repo.getRepoId(), commitId, tagName, tagDescription);
                System.out.println("Tag created.");
                CliUtils.waitForEnter();
            } else if(choice == 14 && loggedIn) {
                List<RepoTag> tags = repositoryService.getTagsByRepositoryId(repo.getRepoId());
                if(tags.isEmpty()) {
                    System.out.println("No tags found.");
                } else {
                    for(RepoTag tag : tags) {
                        System.out.println(tag.getTagId() + " | " + tag.getTagName() + " | commit=" + tag.getCommitId());
                    }
                }
                CliUtils.waitForEnter();
            } else if(choice == 15 && loggedIn) {
                List<RepoWatcher> watchers = repositoryService.getRepositoryWatchers(repo.getRepoId());
                if(watchers.isEmpty()) {
                    System.out.println("No watchers found.");
                } else {
                    for(RepoWatcher watcher : watchers) {
                        System.out.println(watcher.getUserId() + " | " + watcher.getWatchLevel());
                    }
                }
                CliUtils.waitForEnter();

            } else if((loggedIn && choice == 16) || (!loggedIn && choice == 8)) {
                return;
            } else {
                System.out.println("Invalid Choice!!");
                CliUtils.waitForEnter();
            }
        }
    }
}
