CREATE OR REPLACE VIEW secret_pagination AS
SELECT
S.id AS id,
S.id AS secret_id,
S.name AS secret_name,
P.name AS project_name
FROM secret S
LEFT JOIN project P ON S.project_id = P.id;