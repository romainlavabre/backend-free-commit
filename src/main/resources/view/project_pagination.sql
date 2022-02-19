CREATE OR REPLACE VIEW project_pagination AS
SELECT
P.id AS id,
P.id AS project_id,
P.name AS project_name,
B.exit_code AS build_last_exit_code,
B.exit_message AS build_last_exit_message,
B.created_at AS build_last_created_at
FROM project P
LEFT JOIN build B ON B.id = (
    SELECT MAX(id) FROM build WHERE project_id = P.id
);