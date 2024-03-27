CREATE TABLE task_scheduling
(
    id             BIGSERIAL PRIMARY KEY,
    task_id        UUID REFERENCES tasks (id) ON DELETE CASCADE,
    task_date_time TIMESTAMP    NOT NULL,
    task_status    VARCHAR(255) NOT NULL
);
