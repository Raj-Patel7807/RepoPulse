DROP TABLE IF EXISTS pull_request_issue_links CASCADE;
DROP TABLE IF EXISTS user_pinned_repos CASCADE;
DROP TABLE IF EXISTS repo_clones CASCADE;
DROP TABLE IF EXISTS user_blocks CASCADE;
DROP TABLE IF EXISTS user_reports CASCADE;
DROP TABLE IF EXISTS issue_label_mappings CASCADE;
DROP TABLE IF EXISTS issue_labels CASCADE;
DROP TABLE IF EXISTS user_activity_logs CASCADE;
DROP TABLE IF EXISTS notifications CASCADE;
DROP TABLE IF EXISTS repo_watchers CASCADE;
DROP TABLE IF EXISTS user_follows CASCADE;
DROP TABLE IF EXISTS repo_stars CASCADE;
DROP TABLE IF EXISTS discussion_comments CASCADE;
DROP TABLE IF EXISTS repo_issues CASCADE;
DROP TABLE IF EXISTS milestones CASCADE;
DROP TABLE IF EXISTS pull_request_reviews CASCADE;
DROP TABLE IF EXISTS pull_requests CASCADE;
DROP TABLE IF EXISTS branch_merges CASCADE;
DROP TABLE IF EXISTS repo_releases CASCADE;
DROP TABLE IF EXISTS repo_tags CASCADE;
DROP TABLE IF EXISTS file_diffs CASCADE;
DROP TABLE IF EXISTS file_versions CASCADE;
DROP TABLE IF EXISTS repo_files CASCADE;
DROP TABLE IF EXISTS repo_collaborators CASCADE;
DROP TABLE IF EXISTS branches CASCADE;
DROP TABLE IF EXISTS commits CASCADE;
DROP TABLE IF EXISTS repositories CASCADE;
DROP TABLE IF EXISTS users CASCADE;


CREATE TABLE users (
    user_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    profile_bio TEXT,
    profile_avatar_url TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    deleted_at TIMESTAMP
);

CREATE TABLE repositories (
    repository_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    repository_name VARCHAR(100) NOT NULL,
    repository_description TEXT,
    owner_user_id BIGINT NOT NULL,
    visibility_type VARCHAR(10) CHECK (visibility_type IN ('PUBLIC','PRIVATE','INTERNAL')),
	default_branch_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    deleted_at TIMESTAMP,
    deleted_by_user_id BIGINT,
    parent_repository_id BIGINT,
	forked_from_commit_id BIGINT,
    UNIQUE (owner_user_id, repository_name),
    FOREIGN KEY (owner_user_id) REFERENCES users(user_id),
    FOREIGN KEY (deleted_by_user_id) REFERENCES users(user_id),
    FOREIGN KEY (parent_repository_id) REFERENCES repositories(repository_id)
	-- FOREIGN KEY (default_branch_id) REFERENCES branches(branch_id),
	-- FOREIGN KEY (forked_from_commit_id) REFERENCES commits(commit_id)
);

CREATE TABLE commits (
    commit_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    repository_id BIGINT NOT NULL,
    author_user_id BIGINT,
    parent_commit_id BIGINT,
    commit_hash CHAR(40) UNIQUE,
    commit_message TEXT,
    committed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (repository_id) REFERENCES repositories(repository_id),
    FOREIGN KEY (author_user_id) REFERENCES users(user_id),
    FOREIGN KEY (parent_commit_id) REFERENCES commits(commit_id)
);

CREATE TABLE branches (
    branch_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    repository_id BIGINT NOT NULL,
    branch_name VARCHAR(100),
    head_commit_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (repository_id, branch_name),
    FOREIGN KEY (repository_id) REFERENCES repositories(repository_id),
	FOREIGN KEY (head_commit_id) REFERENCES commits(commit_id)
);

CREATE TABLE repo_collaborators (
    repository_id BIGINT,
    user_id BIGINT,
    access_role VARCHAR(15) CHECK (access_role IN ('OWNER','MAINTAINER','WRITE','READ')),
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (repository_id, user_id),
    FOREIGN KEY (repository_id) REFERENCES repositories(repository_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE repo_files (
    file_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    repository_id BIGINT NOT NULL,
    file_name VARCHAR(255),
    file_path TEXT,
    is_binary BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (repository_id, file_path),
    FOREIGN KEY (repository_id) REFERENCES repositories(repository_id)
);

CREATE TABLE file_versions (
    file_version_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    file_id BIGINT,
    commit_id BIGINT,
    content_hash CHAR(64),
    file_size_bytes BIGINT,
    change_type VARCHAR(10) CHECK (change_type IN ('ADDED','MODIFIED','DELETED','RENAMED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (file_id, commit_id),
    FOREIGN KEY (file_id) REFERENCES repo_files(file_id),
    FOREIGN KEY (commit_id) REFERENCES commits(commit_id)
);

CREATE TABLE file_diffs (
    diff_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    old_file_version_id BIGINT,
    new_file_version_id BIGINT,
    diff_content TEXT,
    diff_format VARCHAR(10) CHECK (diff_format IN ('UNIFIED','CONTEXT')),
    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (old_file_version_id) REFERENCES file_versions(file_version_id),
    FOREIGN KEY (new_file_version_id) REFERENCES file_versions(file_version_id)
);

CREATE TABLE repo_tags (
    tag_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    repository_id BIGINT NOT NULL,
    commit_id BIGINT,
    tag_name VARCHAR(100),
    tag_description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (repository_id, tag_name),
    FOREIGN KEY (repository_id) REFERENCES repositories(repository_id),
    FOREIGN KEY (commit_id) REFERENCES commits(commit_id)
);

CREATE TABLE repo_releases (
    release_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    repository_id BIGINT NOT NULL,
    tag_id BIGINT,
    release_title VARCHAR(255),
    release_notes TEXT,
    created_by_user_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (repository_id) REFERENCES repositories(repository_id),
    FOREIGN KEY (tag_id) REFERENCES repo_tags(tag_id),
    FOREIGN KEY (created_by_user_id) REFERENCES users(user_id)
);

CREATE TABLE branch_merges (
    merge_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    repository_id BIGINT,
    source_branch_id BIGINT,
    target_branch_id BIGINT,
    merge_commit_id BIGINT,
    merged_by_user_id BIGINT,
    merge_strategy VARCHAR(10) CHECK (merge_strategy IN ('MERGE','SQUASH','REBASE')),
    merged_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (repository_id) REFERENCES repositories(repository_id),
    FOREIGN KEY (source_branch_id) REFERENCES branches(branch_id),
    FOREIGN KEY (target_branch_id) REFERENCES branches(branch_id),
    FOREIGN KEY (merge_commit_id) REFERENCES commits(commit_id),
    FOREIGN KEY (merged_by_user_id) REFERENCES users(user_id)
);

CREATE TABLE pull_requests (
    pull_request_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    repository_id BIGINT NOT NULL,
    source_branch_id BIGINT,
    target_branch_id BIGINT,
    created_by_user_id BIGINT,
    title VARCHAR(255),
    description TEXT,
    status VARCHAR(10) CHECK (status IN ('OPEN','CLOSED','MERGED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (repository_id) REFERENCES repositories(repository_id),
    FOREIGN KEY (source_branch_id) REFERENCES branches(branch_id),
    FOREIGN KEY (target_branch_id) REFERENCES branches(branch_id),
    FOREIGN KEY (created_by_user_id) REFERENCES users(user_id)
);

CREATE TABLE pull_request_reviews (
    review_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pull_request_id BIGINT,
    reviewer_user_id BIGINT,
    review_comment TEXT,
    review_status VARCHAR(25) CHECK (review_status IN ('APPROVED','CHANGES_REQUESTED','COMMENTED')),
    reviewed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pull_request_id) REFERENCES pull_requests(pull_request_id),
    FOREIGN KEY (reviewer_user_id) REFERENCES users(user_id)
);

CREATE TABLE milestones (
    milestone_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    repository_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    due_date DATE,
    status VARCHAR(10) CHECK (status IN ('OPEN','CLOSED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    closed_at TIMESTAMP,
    UNIQUE (repository_id, title),
    FOREIGN KEY (repository_id) REFERENCES repositories(repository_id)
);

CREATE TABLE repo_issues (
    issue_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    repository_id BIGINT NOT NULL,
    created_by_user_id BIGINT,
    assigned_to_user_id BIGINT,
    title VARCHAR(255),
    description TEXT,
    status VARCHAR(10) CHECK (status IN ('OPEN','CLOSED')),
    priority VARCHAR(10) CHECK (priority IN ('LOW','MEDIUM','HIGH')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    closed_at TIMESTAMP,
    milestone_id BIGINT,
    FOREIGN KEY (repository_id) REFERENCES repositories(repository_id),
    FOREIGN KEY (created_by_user_id) REFERENCES users(user_id),
    FOREIGN KEY (assigned_to_user_id) REFERENCES users(user_id),
    FOREIGN KEY (milestone_id) REFERENCES milestones(milestone_id)
);

CREATE TABLE discussion_comments (
    comment_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT,
    commit_id BIGINT,
    pull_request_id BIGINT,
    issue_id BIGINT,
    parent_comment_id BIGINT,
    comment_body TEXT,
    file_id BIGINT,
    line_number INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (commit_id) REFERENCES commits(commit_id),
    FOREIGN KEY (pull_request_id) REFERENCES pull_requests(pull_request_id),
    FOREIGN KEY (issue_id) REFERENCES repo_issues(issue_id),
    FOREIGN KEY (parent_comment_id) REFERENCES discussion_comments(comment_id),
    FOREIGN KEY (file_id) REFERENCES repo_files(file_id),
    CHECK (
(commit_id IS NOT NULL)::int + (pull_request_id IS NOT NULL)::int + (issue_id IS NOT NULL)::int = 1)
);

CREATE TABLE repo_stars (
    user_id BIGINT,
    repository_id BIGINT,
    starred_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, repository_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (repository_id) REFERENCES repositories(repository_id)
);

CREATE TABLE user_follows (
    follower_user_id BIGINT,
    following_user_id BIGINT,
    followed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (follower_user_id, following_user_id),
    FOREIGN KEY (follower_user_id) REFERENCES users(user_id),
    FOREIGN KEY (following_user_id) REFERENCES users(user_id)
);

CREATE TABLE repo_watchers (
    user_id BIGINT,
    repository_id BIGINT,
    watch_level VARCHAR(15) CHECK (watch_level IN ('ALL','PARTICIPATING','NONE')),
    watched_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, repository_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (repository_id) REFERENCES repositories(repository_id)
);

CREATE TABLE notifications (
    notification_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT,
    notification_type VARCHAR(10) CHECK (notification_type IN ('FOLLOW','PR','ISSUE','STAR','COMMENT')),
    reference_id BIGINT,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE user_activity_logs (
    activity_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT,
    activity_type VARCHAR(20) CHECK (activity_type IN ('CREATE_REPOSITORY','COMMIT','MERGE','FOLLOW')),
    reference_id BIGINT,
    activity_metadata JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE issue_labels (
    label_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    repository_id BIGINT,
    label_name VARCHAR(100),
    label_color CHAR(7),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (repository_id, label_name),
    FOREIGN KEY (repository_id) REFERENCES repositories(repository_id)
);

CREATE TABLE issue_label_mappings (
    issue_id BIGINT,
    label_id BIGINT,
    PRIMARY KEY (issue_id, label_id),
    FOREIGN KEY (issue_id) REFERENCES repo_issues(issue_id),
    FOREIGN KEY (label_id) REFERENCES issue_labels(label_id)
);

CREATE TABLE user_reports (
    report_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    reported_user_id BIGINT,
    reporter_user_id BIGINT,
    report_reason VARCHAR(15) CHECK (report_reason IN ('SPAM','ABUSE','FAKE_ACCOUNT')),
    report_description TEXT,
    report_status VARCHAR(15) CHECK (report_status IN ('OPEN','UNDER_REVIEW','RESOLVED','REJECTED')),
    reviewed_by_admin_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    reviewed_at TIMESTAMP,
    FOREIGN KEY (reported_user_id) REFERENCES users(user_id),
    FOREIGN KEY (reporter_user_id) REFERENCES users(user_id),
    FOREIGN KEY (reviewed_by_admin_id) REFERENCES users(user_id)
);

CREATE TABLE user_blocks (
    blocker_user_id BIGINT,
    blocked_user_id BIGINT,
    blocked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (blocker_user_id, blocked_user_id),
    FOREIGN KEY (blocker_user_id) REFERENCES users(user_id),
    FOREIGN KEY (blocked_user_id) REFERENCES users(user_id)
);

CREATE TABLE repo_clones (
    clone_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    repository_id BIGINT,
    cloned_by_user_id BIGINT,
    clone_type VARCHAR(10) CHECK (clone_type IN ('HTTPS','SSH')),
    cloned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (repository_id) REFERENCES repositories(repository_id),
    FOREIGN KEY (cloned_by_user_id) REFERENCES users(user_id)
);

CREATE TABLE user_pinned_repos (
    user_id BIGINT,
    repository_id BIGINT,
    pinned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, repository_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (repository_id) REFERENCES repositories(repository_id)
);

CREATE TABLE pull_request_issue_links (
    pull_request_id BIGINT,
    issue_id BIGINT,
    link_type VARCHAR(15) CHECK (link_type IN ('CLOSES','REFERENCES')),
    PRIMARY KEY (pull_request_id, issue_id),
    FOREIGN KEY (pull_request_id) REFERENCES pull_requests(pull_request_id),
    FOREIGN KEY (issue_id) REFERENCES repo_issues(issue_id)
);

ALTER TABLE repositories
ADD CONSTRAINT fk_default_branch
FOREIGN KEY (default_branch_id) REFERENCES branches(branch_id);

ALTER TABLE repositories
ADD CONSTRAINT fk_forked_commit_id
FOREIGN KEY (forked_from_commit_id) REFERENCES commits(commit_id);

