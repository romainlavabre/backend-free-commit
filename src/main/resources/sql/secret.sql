SELECT
S.id AS id,
S.id AS secret_id,
S.name AS secret_name,
S.env AS secret_env,
IF(COUNT(SP.secret_id) > 0, "PRIVATE", "PUBLIC") AS secret_scope
FROM secret S
LEFT JOIN secret_project SP ON S.id = SP.secret_id
WHERE {{condition}}
GROUP BY S.id