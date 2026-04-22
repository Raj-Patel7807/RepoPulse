package com.repopulse.repository.cli;

import com.repopulse.infra.session.Authz;
import com.repopulse.infra.util.CliUtils;
import com.repopulse.infra.exception.AppException;
import com.repopulse.file.model.FileDiff;
import com.repopulse.file.model.FileVersion;
import com.repopulse.file.model.RepoFile;
import com.repopulse.file.service.RepoFileService;
import com.repopulse.infra.session.Session;
import com.repopulse.repository.service.RepositoryService;

import java.util.List;

public class RepoFileCLI {
    private final long repoId;
    private final RepoFileService repoFileService = new RepoFileService();
    private final RepositoryService repositoryService = new RepositoryService();

    public RepoFileCLI(long repoId) {
        this.repoId = repoId;
    }

    public void start() {
        while(true) {
            System.out.println("\n=== Code (Files) ===");
            System.out.println("1. List Files");
            System.out.println("2. Add File");
            System.out.println("3. Update File");
            System.out.println("4. Delete File");
            System.out.println("5. File Versions");
            System.out.println("6. File Diffs");
            System.out.println("7. Back");

            int choice = CliUtils.getIntInput("Enter Choice: ");

            if(choice == 1) {
                listFiles();
            } else if(choice == 2) {
                Authz.requireLogin("add file");
                ensureWriteAccess();
                addFile();
            } else if(choice == 3) {
                Authz.requireLogin("update file");
                ensureWriteAccess();
                updateFile();
            } else if(choice == 4) {
                Authz.requireLogin("delete file");
                ensureWriteAccess();
                long fileId = CliUtils.getLongInput("Enter File ID: ");
                repoFileService.deleteFile(fileId);
                CliUtils.waitForEnter();
            } else if(choice == 5) {
                versionsMenu();
            } else if(choice == 6) {
                diffsMenu();
            } else if(choice == 7) {
                return;
            } else {
                System.out.println("Invalid choice!");
                CliUtils.waitForEnter();
            }
        }
    }

    private void listFiles() {
        List<RepoFile> files = repoFileService.getFilesByRepo(repoId);
        if(files.isEmpty()) {
            System.out.println("No files found.");
        } else {
            System.out.println("file_id | name | path | binary");
            for(RepoFile f : files) {
                System.out.println(f.getFileId() + " | " + f.getFileName() + " | " + f.getFilePath() + " | " + (f.isBinary() ? "YES" : "NO"));
            }
        }
        CliUtils.waitForEnter();
    }

    private void addFile() {
        String name = CliUtils.getStringInput("File name: ");
        String path = CliUtils.getStringInput("File path: ");
        int bin = CliUtils.getIntInput("Is binary? (1=yes, 0=no): ");

        RepoFile file = new RepoFile();
        file.setRepoId(repoId);
        file.setFileName(name);
        file.setFilePath(path);
        file.setBinary(bin == 1);

        repoFileService.addFile(file);
        CliUtils.waitForEnter();
    }

    private void updateFile() {
        long fileId = CliUtils.getLongInput("Enter File ID: ");
        RepoFile file = repoFileService.getFile(fileId);
        if(file == null) {
            System.out.println("File not found.");
            CliUtils.waitForEnter();
            return;
        }

        String name = CliUtils.getStringInput("New name: ");
        String path = CliUtils.getStringInput("New path: ");
        int bin = CliUtils.getIntInput("Is binary? (1=yes, 0=no): ");

        file.setFileName(name);
        file.setFilePath(path);
        file.setBinary(bin == 1);
        repoFileService.updateFile(file);
        CliUtils.waitForEnter();
    }

    private void versionsMenu() {
        while(true) {
            System.out.println("\n=== File Versions ===");
            System.out.println("1. List Versions of File");
            System.out.println("2. Create Version");
            System.out.println("3. Back");

            int choice = CliUtils.getIntInput("Enter Choice: ");

            if(choice == 1) {
                long fileId = CliUtils.getLongInput("Enter File ID: ");
                List<FileVersion> versions = repoFileService.getFileVersions(fileId);
                if(versions.isEmpty()) {
                    System.out.println("No versions found.");
                } else {
                    System.out.println("version_id | commit_id | hash | size | type");
                    for(FileVersion v : versions) {
                        System.out.println(v.getFileVersionId() + " | " + v.getCommitId() + " | " + v.getContentHash() + " | " + v.getFileSizeBytes() + " | " + v.getChangeType());
                    }
                }
                CliUtils.waitForEnter();
            } else if(choice == 2) {
                Authz.requireLogin("create file version");
                ensureWriteAccess();
                long fileId = CliUtils.getLongInput("File ID: ");
                long commitId = CliUtils.getLongInput("Commit ID: ");
                String hash = CliUtils.getStringInput("Content hash: ");
                long size = CliUtils.getLongInput("File size bytes: ");
                String changeType = CliUtils.getStringInput("Change type (ADDED/MODIFIED/DELETED/RENAMED): ");
                repoFileService.createFileVersion(fileId, commitId, hash, size, changeType);
                System.out.println("Version created.");
                CliUtils.waitForEnter();
            } else if(choice == 3) {
                return;
            } else {
                System.out.println("Invalid choice!");
                CliUtils.waitForEnter();
            }
        }
    }

    private void diffsMenu() {
        while(true) {
            System.out.println("\n=== File Diffs ===");
            System.out.println("1. Get Diff (oldVersionId + newVersionId)");
            System.out.println("2. Create Diff");
            System.out.println("3. List Diffs for Version");
            System.out.println("4. Back");

            int choice = CliUtils.getIntInput("Enter Choice: ");

            if(choice == 1) {
                long oldId = CliUtils.getLongInput("Old version ID: ");
                long newId = CliUtils.getLongInput("New version ID: ");
                FileDiff diff = repoFileService.getDiff(oldId, newId);
                if(diff == null) {
                    System.out.println("Diff not found.");
                } else {
                    System.out.println("Diff ID: " + diff.getDiffId());
                    System.out.println("Format: " + diff.getDiffFormat());
                    System.out.println("Content:");
                    System.out.println(diff.getDiffContent());
                }
                CliUtils.waitForEnter();
            } else if(choice == 2) {
                Authz.requireLogin("create diff");
                ensureWriteAccess();
                long oldId = CliUtils.getLongInput("Old version ID: ");
                long newId = CliUtils.getLongInput("New version ID: ");
                String format = CliUtils.getStringInput("Format (UNIFIED/CONTEXT): ");
                String content = CliUtils.getStringInput("Diff content: ");
                repoFileService.generateDiff(oldId, newId, content, format);
                System.out.println("Diff created.");
                CliUtils.waitForEnter();
            } else if(choice == 3) {
                long versionId = CliUtils.getLongInput("File version ID: ");
                List<FileDiff> diffs = repoFileService.getDiffsForVersion(versionId);
                if(diffs.isEmpty()) {
                    System.out.println("No diffs found.");
                } else {
                    for(FileDiff d : diffs) {
                        System.out.println(d.getDiffId() + " | " + d.getDiffFormat());
                    }
                }
                CliUtils.waitForEnter();
            } else if(choice == 4) {
                return;
            } else {
                System.out.println("Invalid choice!");
                CliUtils.waitForEnter();
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
