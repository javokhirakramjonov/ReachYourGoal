CREATE TABLE task_plans
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    description TEXT,
    from_date   DATE,
    to_date     DATE,
    user_id     INTEGER REFERENCES users(id) ON DELETE CASCADE
);