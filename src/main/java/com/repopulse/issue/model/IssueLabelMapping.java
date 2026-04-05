package com.repopulse.issue.model;

public class IssueLabelMapping {
    private long issueId;
    private long labelId;

    public IssueLabelMapping() {

    }

    public long getIssueId() {
        return this.issueId;
    }
    public void setIssueId(long issueId) {
        this.issueId = issueId;
    }

    public long getLabelId() {
        return this.labelId;
    }
    public void setLabelId(long labelId) {
        this.labelId = labelId;
    }
}
