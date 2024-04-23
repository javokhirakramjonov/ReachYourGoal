CREATE TABLE task_in_plan
(
    task_id INT REFERENCES tasks(id),
    plan_id INT REFERENCES task_plans(id),
    selected_week_days INT,
    PRIMARY KEY (task_id, plan_id)
);