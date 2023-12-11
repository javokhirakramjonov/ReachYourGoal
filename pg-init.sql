\connect reach_your_goal_database;
CREATE SCHEMA app;

ALTER ROLE javahere SET search_path = app;

GRANT ALL ON DATABASE reach_your_goal_database TO javahere;
GRANT ALL ON SCHEMA app TO javahere;