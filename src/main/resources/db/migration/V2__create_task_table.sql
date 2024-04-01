CREATE TABLE tasks
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(50) NOT NULL,
    description TEXT,
    spent_time  BIGINT       NOT NULL,
    user_id     UUID REFERENCES users (id) ON DELETE CASCADE
);