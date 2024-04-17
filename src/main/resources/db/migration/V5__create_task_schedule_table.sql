CREATE TABLE task_schedules
(
    id             SERIAL PRIMARY KEY,
    task_id        INTEGER REFERENCES tasks (id) ON DELETE CASCADE,
    task_date_time TIMESTAMP   NOT NULL,
    task_status    VARCHAR(50) NOT NULL
);
