CREATE TABLE task_attachments
(
    id      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name    VARCHAR(255) NOT NULL,
    task_id UUID REFERENCES tasks (id) ON DELETE CASCADE
);
