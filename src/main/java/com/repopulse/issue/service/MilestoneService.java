package com.repopulse.issue.service;

import com.repopulse.issue.dao.MilestoneDAO;
import com.repopulse.issue.model.Milestone;
import com.repopulse.issue.validator.MilestoneValidator;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class MilestoneService {
    private final MilestoneDAO dao;

    public MilestoneService() {
        this.dao = new MilestoneDAO();
    }

    public void createMilestone(long repositoryId, String title, String description, Date dueDate) {
        MilestoneValidator.validateTitle(title);

        Milestone m = new Milestone();
        m.setRepositoryId(repositoryId);
        m.setTitle(title.trim());
        m.setDescription(description);
        m.setDueDate(dueDate);
        m.setStatus(Milestone.Status.OPEN);
        m.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        this.dao.createMilestone(m);
    }

    public List<Milestone> getMilestonesByRepo(long repositoryId) {
        return this.dao.getMilestonesByRepo(repositoryId);
    }

    public boolean closeMilestone(long milestoneId) {
        return this.dao.updateMilestoneStatus(milestoneId, "CLOSED");
    }

    public boolean reopenMilestone(long milestoneId) {
        return this.dao.updateMilestoneStatus(milestoneId, "OPEN");
    }
}
