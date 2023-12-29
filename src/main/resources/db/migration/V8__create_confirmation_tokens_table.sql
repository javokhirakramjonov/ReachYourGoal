CREATE TABLE confirmation_tokens
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    token       VARCHAR(255) NOT NULL,
    expire_date BIGINT       NOT NULL,
    user_id     UUID REFERENCES users (id) ON DELETE CASCADE
);