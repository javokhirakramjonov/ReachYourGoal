BEGIN TRANSACTION;

ALTER TABLE users
    ADD COLUMN is_confirmed BOOLEAN DEFAULT true;

UPDATE users
SET is_confirmed = true
WHERE is_confirmed IS NULL;

ALTER TABLE users
    ALTER COLUMN is_confirmed SET NOT NULL;

COMMIT;
