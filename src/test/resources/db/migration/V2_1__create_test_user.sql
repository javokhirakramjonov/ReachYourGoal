INSERT INTO users (id,
                   first_name,
                   last_name,
                   username,
                   email,
                   password,
                   is_account_expired,
                   is_account_locked,
                   is_credentials_expired,
                   is_enabled,
                   role)
VALUES ('f47ac10b-58cc-4372-a567-0e02b2c3d479',
        'mock_firstname',
        'mock_lastname',
        'mock_username',
        'mock_user@mail.com',
        'mock_password',
        false,
        false,
        false,
        true,
        'USER');