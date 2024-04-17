CREATE TABLE task_attachments
(
    id      SERIAL PRIMARY KEY,
    name    VARCHAR(50) NOT NULL,
    task_id INTEGER REFERENCES tasks (id) ON DELETE CASCADE
);
