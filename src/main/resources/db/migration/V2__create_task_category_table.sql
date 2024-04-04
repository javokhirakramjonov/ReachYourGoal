CREATE TABLE task_categories
(
    id                 SERIAL PRIMARY KEY,
    name               VARCHAR(50) NOT NULL,
    parent_category_id INTEGER REFERENCES task_categories (id) ON DELETE CASCADE,
    user_id            INTEGER REFERENCES users (id) ON DELETE CASCADE
);