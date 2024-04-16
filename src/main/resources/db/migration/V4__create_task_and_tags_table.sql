CREATE TABLE tasks_and_tags
(
    task_id INTEGER REFERENCES tasks (id) ON DELETE CASCADE,
    tag_id  INTEGER REFERENCES task_tags (id) ON DELETE CASCADE,
    PRIMARY KEY (task_id, tag_id)
);