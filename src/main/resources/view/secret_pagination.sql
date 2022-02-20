CREATE OR REPLACE VIEW secret_pagination AS
SELECT
S.id AS id,
S.id AS secret_id,
S.name AS secret_name,
IF(COUNT(SP.secret_id) > 0, "PRIVATE", "PUBLIC") AS secret_scope
FROM secret S
LEFT JOIN secret_project SP ON S.id = SP.secret_id
GROUP BY S.id;