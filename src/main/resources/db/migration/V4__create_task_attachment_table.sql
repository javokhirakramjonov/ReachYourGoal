CREATE TABLE task_attachments
(
    id      UUID PRIMARY KEY,
    name    VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    task_id UUID REFERENCES tasks (id) ON DELETE CASCADE
);
