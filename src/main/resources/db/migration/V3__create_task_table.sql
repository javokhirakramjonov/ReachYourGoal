CREATE TABLE tasks
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    description TEXT,
    user_id     INTEGER REFERENCES users (id) ON DELETE CASCADE
);