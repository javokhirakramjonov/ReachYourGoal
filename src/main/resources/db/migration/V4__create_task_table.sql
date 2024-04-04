CREATE TABLE tasks
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    description TEXT,
    spent_time  BIGINT    NOT NULL,
    category_id INTEGER REFERENCES task_categories (id) ON DELETE CASCADE
);