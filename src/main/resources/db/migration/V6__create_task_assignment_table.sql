CREATE TABLE task_assignments
(
    task_id      UUID REFERENCES tasks (id) ON DELETE CASCADE,
    task_date_id SERIAL REFERENCES task_dates (id) ON DELETE CASCADE,
    PRIMARY KEY (task_id, task_date_id)
);
