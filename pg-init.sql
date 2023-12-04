CREATE DATABASE reach_your_goal_database;
\connect reach_your_goal_database;
CREATE SCHEMA app;

CREATE USER javahere WITH PASSWORD 'whynotbro';
ALTER ROLE javahere SET search_path = app;

GRANT ALL ON DATABASE reach_your_goal_database TO javahere;
GRANT ALL ON SCHEMA app TO javahere;