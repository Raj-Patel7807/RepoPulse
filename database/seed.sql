INSERT INTO users (user_id, username, email, password_hash)
OVERRIDING SYSTEM VALUE VALUES
(1,'raj','raj@gmail.com','x'),
(2,'user2','u2@gmail.com','x'),
(3,'user3','u3@gmail.com','x'),
(4,'user4','u4@gmail.com','x'),
(5,'user5','u5@gmail.com','x'),
(6,'user6','u6@gmail.com','x'),
(7,'user7','u7@gmail.com','x'),
(8,'user8','u8@gmail.com','x'),
(9,'user9','u9@gmail.com','x'),
(10,'user10','u10@gmail.com','x');

INSERT INTO repositories (repository_id, repository_name, owner_user_id, visibility_type)
OVERRIDING SYSTEM VALUE VALUES
(1,'RepoPulse',1,'PUBLIC'),
(2,'CP',2,'PUBLIC'),
(3,'Algo',3,'PUBLIC'),
(4,'Web',4,'PRIVATE'),
(5,'Sys',5,'PUBLIC'),
(6,'ML',6,'PUBLIC'),
(7,'OS',7,'PUBLIC'),
(8,'DB',8,'PUBLIC'),
(9,'Graph',9,'PUBLIC'),
(11,'Graph',1,'PUBLIC'),
(12,'Graph',7,'PUBLIC'),
(10,'AI',10,'PUBLIC');

INSERT INTO commits (commit_id, repository_id, author_user_id, parent_commit_id, commit_hash)
OVERRIDING SYSTEM VALUE VALUES
(1,1,1,NULL,'h1'),
(2,1,2,1,'h2'),
(3,2,2,NULL,'h3'),
(4,3,3,NULL,'h4'),
(5,4,4,NULL,'h5'),
(6,5,5,NULL,'h6'),
(7,1,3,2,'h7'),
(8,1,4,7,'h8'),
(9,2,2,3,'h9'),
(10,3,3,4,'h10');

INSERT INTO branches (branch_id, repository_id, branch_name, head_commit_id)
OVERRIDING SYSTEM VALUE VALUES
(1,1,'main',8),
(2,1,'dev',7),
(3,2,'main',9),
(4,3,'main',10),
(5,4,'main',5),
(6,5,'main',6);

UPDATE repositories SET default_branch_id = 1 WHERE repository_id = 1;
UPDATE repositories SET default_branch_id = 3 WHERE repository_id = 2;

INSERT INTO repo_collaborators
OVERRIDING SYSTEM VALUE VALUES
(1,2,'WRITE'),
(1,3,'READ'),
(2,1,'WRITE'),
(3,4,'WRITE');

INSERT INTO repo_files (file_id, repository_id, file_name, file_path)
OVERRIDING SYSTEM VALUE VALUES
(1,1,'main.cpp','/main.cpp'),
(2,1,'util.cpp','/util.cpp'),
(3,2,'cp.cpp','/cp.cpp'),
(4,3,'graph.cpp','/graph.cpp');

INSERT INTO file_versions (file_version_id,file_id,commit_id,content_hash,change_type)
OVERRIDING SYSTEM VALUE VALUES
(1,1,1,'c1','ADDED'),
(2,1,2,'c2','MODIFIED'),
(3,2,2,'c3','ADDED'),
(4,3,3,'c4','ADDED'),
(5,4,4,'c5','ADDED');

INSERT INTO file_diffs (diff_id, old_file_version_id, new_file_version_id, diff_content, diff_format)
OVERRIDING SYSTEM VALUE VALUES
(1,1,2,'diff...','UNIFIED');

INSERT INTO repo_tags (tag_id, repository_id, commit_id, tag_name)
OVERRIDING SYSTEM VALUE VALUES
(1,1,8,'v1.0'),
(2,2,9,'v1.0');

INSERT INTO repo_releases (release_id, repository_id, tag_id, created_by_user_id)
OVERRIDING SYSTEM VALUE VALUES
(1,1,1,1),
(2,2,2,2);

INSERT INTO branch_merges (merge_id, repository_id, source_branch_id, target_branch_id, merge_commit_id, merged_by_user_id, merge_strategy)
OVERRIDING SYSTEM VALUE VALUES
(1,1,2,1,8,1,'MERGE');

INSERT INTO pull_requests (pull_request_id, repository_id, source_branch_id, target_branch_id, created_by_user_id, status)
OVERRIDING SYSTEM VALUE VALUES
(1,1,2,1,2,'MERGED');

INSERT INTO pull_request_reviews (review_id, pull_request_id, reviewer_user_id, review_status)
OVERRIDING SYSTEM VALUE VALUES
(1,1,3,'APPROVED');

INSERT INTO milestones (milestone_id, repository_id, title, status)
OVERRIDING SYSTEM VALUE VALUES
(1,1,'v1','OPEN');

INSERT INTO repo_issues (issue_id, repository_id, created_by_user_id, assigned_to_user_id, status, priority, milestone_id)
OVERRIDING SYSTEM VALUE VALUES
(1,1,1,2,'OPEN','HIGH',1);

INSERT INTO discussion_comments (comment_id,user_id,issue_id,comment_body)
OVERRIDING SYSTEM VALUE VALUES
(1,2,1,'fixing');

INSERT INTO discussion_comments (comment_id,user_id,commit_id,comment_body)
OVERRIDING SYSTEM VALUE VALUES
(2,3,1,'nice');

INSERT INTO discussion_comments (comment_id,user_id,pull_request_id,comment_body)
OVERRIDING SYSTEM VALUE VALUES
(3,4,1,'approved');

INSERT INTO repo_stars
OVERRIDING SYSTEM VALUE VALUES
(2,1),(3,1),(4,1),(5,2);

INSERT INTO user_follows
OVERRIDING SYSTEM VALUE VALUES
(1,2),(2,3),(3,4),(1,3),(1,7),(7,1),(2,4),(3,2);

INSERT INTO repo_watchers
OVERRIDING SYSTEM VALUE VALUES
(1,1,'ALL'),(2,1,'ALL');

INSERT INTO notifications (notification_id,user_id,notification_type,reference_id)
OVERRIDING SYSTEM VALUE VALUES
(1,1,'PR',1),
(2,2,'ISSUE',1);

INSERT INTO user_activity_logs (activity_id,user_id,activity_type,reference_id)
OVERRIDING SYSTEM VALUE VALUES
(1,1,'CREATE_REPOSITORY',1),
(2,2,'COMMIT',2);

INSERT INTO issue_labels (label_id, repository_id, label_name, label_color)
OVERRIDING SYSTEM VALUE VALUES
(1,1,'bug','#ff0000'),
(2,1,'feature','#00ff00');

INSERT INTO issue_label_mappings
OVERRIDING SYSTEM VALUE VALUES
(1,1);

INSERT INTO user_reports (report_id,reported_user_id,reporter_user_id,report_reason,report_status)
OVERRIDING SYSTEM VALUE VALUES
(1,2,1,'SPAM','OPEN');

INSERT INTO user_blocks
OVERRIDING SYSTEM VALUE VALUES
(1,3);

INSERT INTO repo_clones (clone_id,repository_id,cloned_by_user_id,clone_type)
OVERRIDING SYSTEM VALUE VALUES
(1,1,2,'HTTPS');

INSERT INTO user_pinned_repos
OVERRIDING SYSTEM VALUE VALUES
(1,1),(2,2);

INSERT INTO pull_request_issue_links
OVERRIDING SYSTEM VALUE VALUES
(1,1,'CLOSES');
