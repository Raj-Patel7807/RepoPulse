package com.repopulse.cli;

import com.repopulse.model.RepoFile;
import com.repopulse.service.RepoFileService;

import java.util.List;

public class RepoFileCLI {

    private final RepoFileService fileService;
    private final long repoId;

    public RepoFileCLI(long repoId) {
        this.repoId = repoId;
        this.fileService = new RepoFileService();
    }

    public void showMenu() {
        while(true) {
            MenuUtils.clearScreen();

            System.out.println("\n=== Files in Repository ===");
            System.out.println("1. List Files");
            System.out.println("2. Add File");
            System.out.println("3. Update File");
            System.out.println("4. Delete File");
            System.out.println("5. Back");

            int choice = MenuUtils.getIntInput("Enter choice: ");

            if(choice == 1) {
                listFiles();
            } else if(choice == 2) {
                addFile();
            } else if(choice == 3) {
                updateFile();
            } else if(choice == 4) {
                deleteFile();
            } else if(choice == 5) {
                return;
            } else {
                System.out.println("Invalid choice!");
                MenuUtils.waitForEnter();
            }
        }
    }

    private void listFiles() {
        List<RepoFile> files = fileService.getFilesByRepo(repoId);

        if(files.isEmpty()) {
            System.out.println("No files found in this repository.");
        } else {
            System.out.println("Files:");
            for(RepoFile f : files) {
                System.out.println(f.getFileId() + " | " + f.getFileName() + " | Path: " + f.getFilePath() + " | Binary: " + f.isBinary());
            }
        }

        MenuUtils.waitForEnter();
    }

    private void addFile() {
        String fileName = MenuUtils.getStringInput("File Name: ");
        String filePath = MenuUtils.getStringInput("File Path: ");
        boolean isBinary = MenuUtils.getStringInput("Is Binary? (true/false): ").equalsIgnoreCase("true");

        RepoFile file = new RepoFile(0, repoId, fileName, filePath, isBinary);
        fileService.addFile(file);

        MenuUtils.waitForEnter();
    }

    private void updateFile() {
        long fileId = MenuUtils.getIntInput("Enter File ID to update: ");
        RepoFile file = fileService.getFile(fileId);

        if(file == null) {
            System.out.println("File not found!");
            MenuUtils.waitForEnter();
            return;
        }

        String newFileName = MenuUtils.getStringInput("New File Name (" + file.getFileName() + "): ");
        String newFilePath = MenuUtils.getStringInput("New File Path (" + file.getFilePath() + "): ");
        boolean isBinary = MenuUtils.getStringInput("Is Binary? (true/false) (" + file.isBinary() + "): ").equalsIgnoreCase("true");

        if(!newFileName.isBlank()) {
            file.setFileName(newFileName);
        }
        if(!newFilePath.isBlank()) {
            file.setFilePath(newFilePath);
        }
        file.setBinary(isBinary);

        fileService.updateFile(file);

        MenuUtils.waitForEnter();
    }

    private void deleteFile() {
        long fileId = MenuUtils.getIntInput("Enter File ID to delete: ");
        fileService.deleteFile(fileId);

        MenuUtils.waitForEnter();
    }
}
