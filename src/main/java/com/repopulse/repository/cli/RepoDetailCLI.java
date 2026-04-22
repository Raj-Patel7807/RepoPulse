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
import com.repopulse.repository.model.Repository;
import com.repopulse.repository.service.RepositoryService;

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
                System.out.println("11. Back");
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

            } else if((loggedIn && choice == 11) || (!loggedIn && choice == 8)) {
                return;
            } else {
                System.out.println("Invalid Choice!!");
                CliUtils.waitForEnter();
            }
        }
    }
}
