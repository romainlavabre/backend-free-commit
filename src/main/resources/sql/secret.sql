SELECT
S.id AS id,
S.id AS secret_id,
S.name AS secret_name,
S.env AS secret_env,
IF((SELECT COUNT(SP.secret_id) FROM secret_project SP WHERE S.id = SP.secret_id ) > 0, "PRIVATE", "PUBLIC") AS secret_scope
FROM secret S
WHERE {{condition}}