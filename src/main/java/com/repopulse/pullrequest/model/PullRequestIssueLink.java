package com.repopulse.pullrequest.model;

public class PullRequestIssueLink {
    private long pullRequestId;
    private long issueId;
    private LinkType linkType;

    public PullRequestIssueLink() {

    }

    public long getPullRequestId() {
        return this.pullRequestId;
    }

    public void setPullRequestId(long pullRequestId) {
        this.pullRequestId = pullRequestId;
    }

    public long getIssueId() {
        return this.issueId;
    }

    public void setIssueId(long issueId) {
        this.issueId = issueId;
    }

    public LinkType getLinkType() {
        return this.linkType;
    }

    public void setLinkType(LinkType linkType) {
        this.linkType = linkType;
    }

    public enum LinkType {
        CLOSES, REFERENCES
    }
}
