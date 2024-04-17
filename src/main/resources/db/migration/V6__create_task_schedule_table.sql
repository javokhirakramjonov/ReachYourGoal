CREATE TABLE task_schedules
(
    id          SERIAL PRIMARY KEY,
    task_id     INTEGER REFERENCES tasks (id) ON DELETE CASCADE,
    task_plan_id     INTEGER REFERENCES task_plans (id) ON DELETE CASCADE,
    task_date   DATE        NOT NULL,
    task_status VARCHAR(50) NOT NULL
);
