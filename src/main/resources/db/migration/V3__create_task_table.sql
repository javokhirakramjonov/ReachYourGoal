CREATE TABLE tasks
(
    id          UUID PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    spent_time  BIGINT       NOT NULL,
    status      VARCHAR(255) NOT NULL,
    user_id     UUID REFERENCES users (id) ON DELETE CASCADE
);