CREATE TABLE users
(
    id                     UUID PRIMARY KEY,
    first_name             VARCHAR(255) NOT NULL,
    last_name              VARCHAR(255) NOT NULL,
    username               VARCHAR(255) NOT NULL,
    email                  VARCHAR(255) NOT NULL,
    password               VARCHAR(255) NOT NULL,
    is_account_expired     BOOLEAN      NOT NULL,
    is_account_locked      BOOLEAN      NOT NULL,
    is_credentials_expired BOOLEAN      NOT NULL,
    is_enabled             BOOLEAN      NOT NULL,
    role                   VARCHAR(255) NOT NULL
);
