CREATE TABLE users
(
    id                     UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name             VARCHAR(255) NOT NULL,
    last_name              VARCHAR(255) NOT NULL,
    username               VARCHAR(255) NOT NULL,
    email                  VARCHAR(255) NOT NULL,
    password               VARCHAR(255) NOT NULL,
    is_account_expired     BOOLEAN      NOT NULL,
    is_account_locked      BOOLEAN      NOT NULL,
    is_credentials_expired BOOLEAN      NOT NULL,
    is_enabled             BOOLEAN      NOT NULL,
    role                   VARCHAR(255) NOT NULL,
    is_confirmed           BOOLEAN      NOT NULL,
    created_at             TIMESTAMP    NOT NULL
);
