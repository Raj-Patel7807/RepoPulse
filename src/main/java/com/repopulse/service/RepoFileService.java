package com.repopulse.service;

import com.repopulse.dao.RepoFileDAO;
import com.repopulse.model.RepoFile;

import java.sql.SQLException;
import java.util.List;

public class RepoFileService {
    private final RepoFileDAO fileDAO;

    public RepoFileService() {
        this.fileDAO = new RepoFileDAO();
    }

    public void addFile(RepoFile file) {
        try {
            fileDAO.createFile(file);
            System.out.println("File added successfully with ID: " + file.getFileId());
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public RepoFile getFile(long fileId) {
        try {
            return fileDAO.getFileById(fileId);
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<RepoFile> getFilesByRepo(long repoId) {
        try {
            return fileDAO.getFilesByRepo(repoId);
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return List.of();
    }

    public void updateFile(RepoFile file) {
        try {
            fileDAO.updateFile(file);
            System.out.println("File updated successfully!");
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFile(long fileId) {
        try {
            fileDAO.deleteFile(fileId);
            System.out.println("File deleted successfully!");
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
