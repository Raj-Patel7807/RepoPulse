package com.repopulse.dao;

import com.repopulse.model.Commit;

import java.util.List;

public class CommitDAOTest {

    public static void main(String[] args) {
        CommitDAO dao = new CommitDAO();

        Commit commit = new Commit();
        commit.setRepoId(1);
        commit.setAuthorUserId(1);
        commit.setCommitMessage("Initial commit");

        dao.createCommit(commit);

//        List<Commit> commits = dao.getCommitsByRepoId(1);
//
//        System.out.println("Commits fetched: " + commits.size());
//        for (Commit c : commits) {
//            System.out.println(c.getCommitMessage());
//        }
    }
}