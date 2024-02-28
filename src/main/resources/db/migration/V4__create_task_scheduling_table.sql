CREATE TABLE task_scheduling
(
    task_id   UUID REFERENCES tasks (id) ON DELETE CASCADE,
    task_date DATE         NOT NULL,
    task_time TIME         NOT NULL,
    task_status    VARCHAR(255) NOT NULL,
    PRIMARY KEY (task_id, task_date, task_time)
);
